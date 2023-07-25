package com.severett.archunitdemo.eventsourcing

import com.severett.archunitdemo.eventsourcing.domain.event.StockEvent
import com.severett.archunitdemo.eventsourcing.repo.ReadRepo
import com.severett.archunitdemo.eventsourcing.repo.WriteRepo
import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaField
import com.tngtech.archunit.core.domain.JavaMember
import com.tngtech.archunit.core.domain.JavaModifier
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import kotlinx.serialization.KSerializer
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.*
import kotlin.coroutines.Continuation
import kotlin.test.Test

private const val BASE_PACKAGE = "com.severett.archunitdemo.eventsourcing"
private const val CONTROLLER_PACKAGE = "$BASE_PACKAGE.controller"
private const val SERDE_PACKAGE = "$BASE_PACKAGE.serde"
private const val SERVICE_PACKAGE = "$BASE_PACKAGE.service"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventSourcingArchTest {
    private val eventSourcingClasses = ClassFileImporter().importPackages(BASE_PACKAGE)

    @Test
    fun serializerStyle() {
        val beAnObject = object : ArchCondition<JavaClass>("be an object") {
            override fun check(klass: JavaClass, events: ConditionEvents) {
                val hasSinglePrivateConstructor = try {
                    klass.constructor.modifiers.any { it == JavaModifier.PRIVATE }
                } catch (e: Exception) {
                    false
                }
                val hasStaticInstance = klass.fields.any { field ->
                    field.name == "INSTANCE" && field.modifiers.all { modifier ->
                        modifier == JavaModifier.STATIC || modifier != JavaModifier.PRIVATE
                    }
                }
                events.add(
                    SimpleConditionEvent(
                        klass,
                        hasSinglePrivateConstructor && hasStaticInstance,
                        "${klass.name} must use the object keyword"
                    )
                )
            }
        }

        val styleRule = classes()
            .that()
            .resideInAPackage(SERDE_PACKAGE)
            .should()
            .implement(KSerializer::class.java)
            .andShould(beAnObject)

        styleRule.check(eventSourcingClasses)
    }

    @ParameterizedTest
    @ValueSource(strings = ["command", "query"])
    fun controllerConstructor(pkg: String) {
        val classPkg = "$SERVICE_PACKAGE.$pkg"
        val thatResideInCorrectServicePackage = object : DescribedPredicate<List<JavaClass>>(
            "must reside in package $classPkg"
        ) {
            override fun test(args: List<JavaClass>) = args.all { klass ->
                klass.`package`.name == classPkg
            }
        }

        val controllerRule = constructors()
            .that()
            .areDeclaredInClassesThat()
            .resideInAPackage("$CONTROLLER_PACKAGE.$pkg")
            .should()
            .haveRawParameterTypes(thatResideInCorrectServicePackage)

        controllerRule.check(eventSourcingClasses)
    }

    @Test
    fun immutableDataInput() {
        val immutableDataRule = classes()
            .that()
            .implement(StockEvent::class.java)
            .should()
            .haveOnlyFinalFields()
            .andShould()
            .haveModifier(JavaModifier.FINAL)

        immutableDataRule.check(eventSourcingClasses)
    }

    @Test
    fun cqrsCompliance() {
        val areOfReadRepo = object : DescribedPredicate<JavaMember>(
            "are of class ${ReadRepo::class.simpleName}"
        ) {
            override fun test(member: JavaMember): Boolean {
                return (member is JavaField) && member.rawType.isEquivalentTo(ReadRepo::class.java)
            }

        }

        val containMembersOfWriteRepo = object : ArchCondition<JavaClass>(
            "contain members of class ${WriteRepo::class.simpleName}"
        ) {
            override fun check(klass: JavaClass, events: ConditionEvents) {
                events.add(
                    SimpleConditionEvent(
                        klass,
                        klass.fields.any { field -> field.rawType.isEquivalentTo(WriteRepo::class.java) },
                        "${klass.name} cannot have both ${ReadRepo::class.simpleName} " +
                                "and ${WriteRepo::class.simpleName}"
                    )
                )
            }
        }

        val complianceRule = noClasses()
            .that()
            .containAnyMembersThat(areOfReadRepo)
            .should(containMembersOfWriteRepo)

        complianceRule.check(eventSourcingClasses)
    }

    @Test
    fun coroutineControllersCompliance() {
        val thatDoesNotUseReactor = object : DescribedPredicate<JavaClass>("does not use Reactor") {
            override fun test(klass: JavaClass): Boolean {
                return !klass.isEquivalentTo(Mono::class.java) && !klass.isEquivalentTo(Flux::class.java)
            }
        }
        val returnSignatureCheck = methods()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(RestController::class.java)
            .and()
            .arePublic()
            .should()
            .haveRawReturnType(thatDoesNotUseReactor)

        val forTheSuspendKeyword = object : DescribedPredicate<List<JavaClass>>(
            "be used for the suspend keyword"
        ) {
            override fun test(klasses: List<JavaClass>) = klasses.any {
                klass -> klass.isEquivalentTo(Continuation::class.java)
            }
        }
        val suspendKeywordCheck = methods()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(RestController::class.java)
            .and()
            .arePublic()
            .should()
            .haveRawParameterTypes(forTheSuspendKeyword)

        assertAll(
            { returnSignatureCheck.check(eventSourcingClasses) },
            { suspendKeywordCheck.check(eventSourcingClasses) }
        )
    }
}

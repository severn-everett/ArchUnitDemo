package com.severett.archunitdemo.hexagonal

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.*
import kotlin.metadata.Visibility
import kotlin.metadata.jvm.KotlinClassMetadata
import kotlin.metadata.visibility
import kotlin.test.Test

private const val BASE_PACKAGE = "com.severett.archunitdemo.hexagonal"
private const val ADAPTER_PACKAGE = "$BASE_PACKAGE.adapter"
private const val PORT_PACKAGE = "$BASE_PACKAGE.port"
private const val SERVICE_PACKAGE = "$BASE_PACKAGE.service"
private const val USE_CASE_PACKAGE = "$BASE_PACKAGE.usecase"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HexagonalArchTest {
    private val hexagonalClasses = ClassFileImporter().importPackages(BASE_PACKAGE)
    private val areNonSynthetic = object : DescribedPredicate<JavaClass>("is not a synthetic class") {
        override fun test(klass: JavaClass) = klass.checkMetadata<KotlinClassMetadata.Class>()
    }

    @Test
    fun noImplementationLeakage() {
        val portDependenciesOnlyRule = noClasses()
            .that()
            .resideOutsideOfPackage(ADAPTER_PACKAGE)
            .should()
            .dependOnClassesThat()
            .resideInAPackage(ADAPTER_PACKAGE)

        val beInternal = object : ArchCondition<JavaClass>("use the internal keyword") {
            override fun check(klass: JavaClass, events: ConditionEvents) {
                events.add(
                    SimpleConditionEvent(
                        klass,
                        klass.checkMetadata<KotlinClassMetadata.Class> { it.kmClass.visibility == Visibility.INTERNAL },
                        "${klass.name} must use the internal keyword"
                    )
                )
            }
        }

        val internalOnlyRule = classes()
            .that()
            .resideInAPackage(ADAPTER_PACKAGE)
            .and(areNonSynthetic)
            .and()
            .areNotAnonymousClasses()
            .and()
            .arePublic()
            .should(beInternal)

        assertAll(
            { portDependenciesOnlyRule.check(hexagonalClasses) },
            { internalOnlyRule.check(hexagonalClasses) }
        )
    }

    @Test
    fun useCaseConstructors() {
        val thatResideInPortOrServicePackages = object : DescribedPredicate<List<JavaClass>>(
            "must reside in the port or service package"
        ) {
            override fun test(args: List<JavaClass>) = args.all { klass ->
                klass.`package`.name in listOf(PORT_PACKAGE, SERVICE_PACKAGE)
            }
        }

        val constructorRule = constructors()
            .that()
            .areDeclaredInClassesThat()
            .resideInAPackage(USE_CASE_PACKAGE)
            .and()
            .areDeclaredInClassesThat()
            .areNotAnonymousClasses()
            .should()
            .haveRawParameterTypes(thatResideInPortOrServicePackages)

        constructorRule.check(hexagonalClasses)
    }

    @ParameterizedTest
    @MethodSource
    fun classNomenclature(packageName: String, requiredSuffix: String) {
        val suffixRule = classes()
            .that()
            .resideInAPackage(packageName)
            .and(areNonSynthetic)
            .and()
            .areNotAnonymousClasses()
            .should()
            .haveSimpleNameEndingWith(requiredSuffix)

        suffixRule.check(hexagonalClasses)
    }

    private fun classNomenclature() = Stream.of(
        Arguments.of(ADAPTER_PACKAGE, "Adapter"),
        Arguments.of(PORT_PACKAGE, "Port"),
    )

    private inline fun <reified T> JavaClass.checkMetadata(predicate: (T) -> Boolean = { true }): Boolean {
        return annotations
            .asSequence()
            .filter { it.rawType.isAssignableTo(Metadata::class.java) }
            .any { annotation ->
                val metadataAnnotation = annotation.`as`(Metadata::class.java)
                val metadata = KotlinClassMetadata.readStrict(metadataAnnotation)
                (metadata as? T)?.let { predicate.invoke(it) } ?: false
            }
    }
}

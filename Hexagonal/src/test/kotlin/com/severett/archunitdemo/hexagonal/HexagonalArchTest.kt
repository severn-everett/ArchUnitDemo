package com.severett.archunitdemo.hexagonal

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import kotlinx.metadata.Visibility
import kotlinx.metadata.jvm.KotlinClassMetadata
import kotlinx.metadata.visibility
import org.junit.jupiter.api.assertAll
import kotlin.test.Test

private const val ADAPTER_PACKAGE = "com.severett.archunitdemo.hexagonal.adapter"

class HexagonalArchTest {
    private val hexagonalClasses = ClassFileImporter().importPackages("com.severett.archunitdemo.hexagonal")

    @Test
    fun noImplementationLeakage() {
        val portDependenciesOnlyRule = noClasses()
            .that()
            .resideOutsideOfPackage(ADAPTER_PACKAGE)
            .should()
            .dependOnClassesThat()
            .resideInAPackage(ADAPTER_PACKAGE)

        val areNonSynthetic = object : DescribedPredicate<JavaClass>("is not a synthetic class") {
            override fun test(klass: JavaClass) = klass.checkMetadata { it is KotlinClassMetadata.Class }
        }

        val beInternal = object : ArchCondition<JavaClass>("use the internal keyword") {
            override fun check(klass: JavaClass, events: ConditionEvents) {
                events.add(
                    SimpleConditionEvent(
                        klass,
                        klass.checkMetadata {
                            (it as KotlinClassMetadata.Class).kmClass.visibility == Visibility.INTERNAL
                        },
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
            .areTopLevelClasses()
            .and()
            .arePublic()
            .should(beInternal)

        assertAll(
            { portDependenciesOnlyRule.check(hexagonalClasses) },
            { internalOnlyRule.check(hexagonalClasses) }
        )
    }

    private inline fun JavaClass.checkMetadata(
        predicate: (KotlinClassMetadata) -> Boolean
    ): Boolean {
        return annotations
            .asSequence()
            .filter { it.rawType.isAssignableTo(Metadata::class.java) }
            .any { annotation ->
                val metadataAnnotation = annotation.`as`(Metadata::class.java)
                val metadata = KotlinClassMetadata.read(metadataAnnotation)
                predicate.invoke(metadata)
            }
    }
}

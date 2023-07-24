package com.severett.archunitdemo.activerecord

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import jakarta.persistence.Entity
import org.junit.jupiter.api.assertAll
import org.springframework.data.repository.Repository
import org.springframework.web.bind.annotation.RestController
import kotlin.test.Test

private const val REPO_PACKAGE = "com.severett.archunitdemo.activerecord.repo"
private const val DOMAIN_PACKAGE = "com.severett.archunitdemo.activerecord.model.domain"
private const val DTO_PACKAGE = "com.severett.archunitdemo.activerecord.model.dto"

class ActiveRecordArchTest {
    private val activeRecordClasses = ClassFileImporter().importPackages("com.severett.archunitdemo.activerecord")

    @Test
    fun repositoryInheritance() {
        val repoInheritanceRule = classes()
            .that()
            .resideInAPackage(REPO_PACKAGE)
            .should()
            .beInterfaces()
            .andShould()
            .beAssignableTo(Repository::class.java)

        val onlyInRepoRule = classes()
            .that()
            .resideOutsideOfPackage(REPO_PACKAGE)
            .should()
            .notBeAssignableTo(Repository::class.java)

        assertAll(
            { repoInheritanceRule.check(activeRecordClasses) },
            { onlyInRepoRule.check(activeRecordClasses) },
        )
    }

    @Test
    fun noDomainLeakage() {
        val dtoPredicate = object : DescribedPredicate<JavaClass>("be either a standard collection or a DTO") {
            override fun test(t: JavaClass): Boolean {
                return when {
                    t.packageName == DTO_PACKAGE -> true
                    t.packageName == "java.lang" && t.isAssignableTo("void") -> true
                    t.isAssignableTo(Collection::class.java) -> true
                    else -> false
                }
            }
        }

        val dtoAnnotationRule = classes()
            .that()
            .resideInAPackage(DTO_PACKAGE)
            .should()
            .notBeAnnotatedWith(Entity::class.java)

        val domainAnnotationRule = classes()
            .that()
            .resideInAPackage(DOMAIN_PACKAGE)
            .should()
            .beAnnotatedWith(Entity::class.java)
            .orShould()
            .beEnums()

        val returnTypeRule = methods()
            .that()
            .arePublic()
            .and()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(RestController::class.java)
            .should()
            .haveRawReturnType(dtoPredicate)

        assertAll(
            { dtoAnnotationRule.check(activeRecordClasses) },
            { domainAnnotationRule.check(activeRecordClasses) },
            { returnTypeRule.check(activeRecordClasses) },
        )
    }
}
package com.severett.archunitdemo.activerecord

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction
import com.tngtech.archunit.library.Architectures
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import jakarta.persistence.Entity
import org.junit.jupiter.api.assertAll
import org.springframework.data.repository.Repository
import org.springframework.web.bind.annotation.RestController

private const val BASE_PACKAGE = "com.severett.archunitdemo.activerecord"
private const val CONTROLLER_PACKAGE = "$BASE_PACKAGE.controller"
private const val REPO_PACKAGE = "$BASE_PACKAGE.repo"
private const val SERVICE_PACKAGE = "$BASE_PACKAGE.service"
private const val DOMAIN_PACKAGE = "$BASE_PACKAGE.model.domain"
private const val DTO_PACKAGE = "$BASE_PACKAGE.model.dto"

private const val CONTROLLER_LAYER = "Controller"
private const val REPO_LAYER = "Repo"
private const val SERVICE_LAYER = "Service"

@AnalyzeClasses(packagesOf = [ActiveRecordApp::class])
class ActiveRecordArchTest {
    @ArchTest
    val correctLayering: Architectures.LayeredArchitecture = layeredArchitecture()
        .consideringAllDependencies()
        .layer(CONTROLLER_LAYER).definedBy(CONTROLLER_PACKAGE)
        .layer(SERVICE_LAYER).definedBy(SERVICE_PACKAGE)
        .layer(REPO_LAYER).definedBy(REPO_PACKAGE)
        .whereLayer(CONTROLLER_LAYER).mayNotBeAccessedByAnyLayer()
        .whereLayer(SERVICE_LAYER).mayOnlyBeAccessedByLayers(CONTROLLER_LAYER)
        .whereLayer(REPO_LAYER).mayOnlyBeAccessedByLayers(SERVICE_LAYER)

    @ArchTest
    val repositoryInheritance: ClassesShouldConjunction = classes()
        .that()
        .resideInAPackage(REPO_PACKAGE)
        .should()
        .beInterfaces()
        .andShould()
        .beAssignableTo(Repository::class.java)

    @ArchTest
    val onlyImplementRepoInRepoPackage: ClassesShouldConjunction = classes()
        .that()
        .resideOutsideOfPackage(REPO_PACKAGE)
        .should()
        .notBeAssignableTo(Repository::class.java)

    @ArchTest
    fun noDomainLeakage(importedClasses: JavaClasses) {
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
            { dtoAnnotationRule.check(importedClasses) },
            { domainAnnotationRule.check(importedClasses) },
            { returnTypeRule.check(importedClasses) },
        )
    }
}

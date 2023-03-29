package com.severett.archunitdemo.activerecord

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import org.springframework.stereotype.Controller
import kotlin.test.Test

class ArchTest {
    @Test
    fun aTest() {
        val dtoPredicate = object : DescribedPredicate<JavaClass>("be either a standard collection or a DTO") {
            override fun test(t: JavaClass): Boolean {
                return true
            }
        }
        methods()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(Controller::class.java)
            .and()
            .arePublic()
            .should()
            .haveRawReturnType(dtoPredicate)
    }
}
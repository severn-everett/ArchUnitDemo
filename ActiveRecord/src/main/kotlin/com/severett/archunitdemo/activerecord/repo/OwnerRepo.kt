package com.severett.archunitdemo.activerecord.repo

import com.severett.archunitdemo.activerecord.model.domain.Owner
import org.springframework.data.jpa.repository.JpaRepository

interface OwnerRepo : JpaRepository<Owner, Long>

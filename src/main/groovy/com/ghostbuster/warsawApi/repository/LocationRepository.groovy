package com.ghostbuster.warsawApi.repository

import com.ghostbuster.warsawApi.domain.internal.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository  extends JpaRepository<Location, Long> {

    List<Location> findByAddress(String address)
}

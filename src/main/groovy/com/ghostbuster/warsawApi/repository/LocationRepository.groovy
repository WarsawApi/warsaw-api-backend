package com.ghostbuster.warsawApi.repository

import com.ghostbuster.warsawApi.domain.internal.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository extends JpaRepository<Location, String> {

    Location findByAddress(String address)

    List<Location> findByAddressIn(List<String> addresses)
}

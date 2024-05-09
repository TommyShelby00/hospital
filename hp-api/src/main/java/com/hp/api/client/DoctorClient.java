package com.hp.api.client;

import com.hp.api.vo.Doctor;
import com.hp.api.vo.Organization;
import com.hp.api.vo.OrganizationVO;
import com.hp.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("doctor-service")
public interface DoctorClient {
    @GetMapping("/doctor/{id}")
    Result<Doctor> getById(@PathVariable("id") Integer id);

    @GetMapping("/doctor")
    Result<List<Doctor>> getByOrgId(@RequestParam Integer id);

    @GetMapping("/organization")
    Result<List<OrganizationVO>> getAll();

    @GetMapping("/organization/{id}")
    Result<Organization> getById1(@PathVariable Integer id);

}

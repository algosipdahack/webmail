package kr.co.ggabi.springboot.dto;

import kr.co.ggabi.springboot.domain.users.Address;

import java.util.Date;

public class AddressResponseDto {
    public Long id;
    public Long parentId;
    public String username;
    public String nickname;
    public String phone;
    public String department;
    public String position;
    public String company;
    public Date isCreated;

    public AddressResponseDto(Address address){
        this.id = address.getId();
        this.username = address.getUsername();
        this.nickname = address.getNickname();
        this.department = address.getDepartment();
        this.position = address.getPosition();
        this.phone = address.getPhone();
        this.parentId = address.getParentId();
        this.company = address.getCompany();
        this.isCreated = address.getIsCreated();
    }
}

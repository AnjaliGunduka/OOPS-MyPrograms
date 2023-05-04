package com.junodx.api.controllers.users.payloads;

import com.junodx.api.models.auth.Authority;

public class RolePayload {
    private Authority role;
    

    public RolePayload() {
		super();
	}

	public RolePayload(Authority role) {
		super();
		this.role = role;
	}

	public Authority getRole() {
        return role;
    }

    public void setRole(Authority role) {
        this.role = role;
    }
}

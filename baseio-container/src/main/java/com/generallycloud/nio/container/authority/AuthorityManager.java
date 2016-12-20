/*
 * Copyright 2015 GenerallyCloud.com
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package com.generallycloud.nio.container.authority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.generallycloud.nio.common.StringUtil;

public class AuthorityManager {

	private Integer				roleID			;
	
	private Authority				authority			;

	private Map<String, Permission>	permissions		= new HashMap<String, Permission>();

	private List<Permission>			permissionsList	= new ArrayList<Permission>();

	protected void addPermission(Permission permission) {
		
		if (permissions.containsKey(permission.getPermissionAPI())) {
			return;
		}
		
		permissions.put(permission.getPermissionAPI(), permission);
		permissionsList.add(permission);
	}

	protected Integer getRoleID() {
		return roleID;
	}

	public boolean isInvokeApproved(String permissionAPI) {
		
		if (!isNeedAuthor(permissionAPI)) {
			return true;
		}

		Permission permission = permissions.get(permissionAPI);

		return permission != null && permission.invoke();
	}

	protected void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}

	@Override
	protected AuthorityManager clone() {
		AuthorityManager manager = new AuthorityManager();

		manager.setRoleID(roleID);

		for (Permission p : permissionsList) {
			manager.addPermission(p.clone());
		}

		return manager;
	}
	
	private boolean isNeedAuthor(String permissionAPI){
		return StringUtil.isNullOrBlank(permissionAPI) || permissionAPI.endsWith(".auth");
	}
	
	protected void setAuthority(Authority authority){
		this.authority = authority;
	}
	
	public Authority getAuthority(){
		return authority;
	}

}

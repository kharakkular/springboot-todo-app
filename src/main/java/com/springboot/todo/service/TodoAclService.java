package com.springboot.todo.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboot.todo.entity.Todo;

@Service
public class TodoAclService {
	Logger logger = LogManager.getLogger(TodoAclService.class);
	
	private final MutableAclService aclService;
	
	public TodoAclService(MutableAclService aclService) {
		this.aclService = aclService;
	}
	
	public void grantReadPermission(Todo todo, String username) {
		ObjectIdentity oid = new ObjectIdentityImpl(Todo.class, todo.getId());
		MutableAcl acl = null;
		
		try {
			acl = (MutableAcl) aclService.readAclById(oid);
		} catch (NotFoundException e) {
			logger.info("*********************************Logging exception from TodoAclService, exception: " + e.getMessage());
			acl = aclService.createAcl(oid);
		}
//		List<Sid> sid = new SidRetrievalStrategyImpl().getSids(SecurityContextHolder.getContext().getAuthentication());
		Sid sid = new PrincipalSid(username);
		acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
		aclService.updateAcl(acl);
	}
}

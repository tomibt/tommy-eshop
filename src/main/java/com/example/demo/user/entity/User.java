package com.example.demo.user.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	

	public User(User users) {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String firstName;
	
	
	private String lastName;
	
	
	@Column(unique = true)
	private String username;
	
	
	private String password;
	
	@NaturalId
	@Email
	@NotBlank
	private String email;
	
	@Lob
	@Column
	private String image;
	
	private String mobile;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(referencedColumnName = "id")
	private Address address;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime date;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<>() ;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(referencedColumnName = "id")
	private CreditCard cc;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
	private List<WishList> wishList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.MERGE )
	private List<CartProduct> cart;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL )
	private List<Order> orders;
	
	

}

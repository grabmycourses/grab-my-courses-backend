package open.seats.tracker.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import open.seats.tracker.model.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 165464467L;

	private int userId;

	private String email;

	@JsonIgnore
	private String password;
	
	private String fullname;
	
	private boolean verified;

	public UserDetailsImpl(int userId, String email, String password, String fullName, boolean verifiedStatus) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.fullname = fullName;
		this.verified = verifiedStatus;
	}

	public static UserDetailsImpl build(User user) {
		return new UserDetailsImpl(user.getUserId(), user.getEmail(), user.getPassword(), user.getFullName(), user.isVerified());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}
	
	public int getUserId() {
		return userId;
	}

	public String getFullname() {
		return fullname;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	public boolean isVerified() {
		return verified;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(userId, user.userId);
	}

}

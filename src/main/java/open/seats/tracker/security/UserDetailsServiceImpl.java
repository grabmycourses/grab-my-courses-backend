package open.seats.tracker.security;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import open.seats.tracker.model.User;
import open.seats.tracker.repository.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		List<User> userEntries = usersRepository.findByEmailAndActiveOrderByCreatedTimestampDesc(email, true);
		
		if(CollectionUtils.isEmpty(userEntries)) {
			throw new UsernameNotFoundException("User Not Found with email:" + email);
		}
		
		User mainAccount = userEntries.get(0);
		for(User thisUser : userEntries) {
			if(thisUser.isVerified()) {
				mainAccount = thisUser;
				break;
			}
		}

		return UserDetailsImpl.build(mainAccount);
	}
	
	@Transactional
	public User loadUserByUserNameAndVerified(String email) {
		return usersRepository.findByEmailAndActiveAndVerified(email, true, true)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email:" + email));
	}

}

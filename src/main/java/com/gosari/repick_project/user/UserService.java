package com.gosari.repick_project.user;
import com.gosari.repick_project.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    public Object modify;
    SiteUser user = new SiteUser();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password, String nickname) {
        user.getId();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    /*SiteUser를 조회할수있는 getUser메서드*/
    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if(siteUser.isPresent()){
            return siteUser.get();
        }else{
            throw new DataNotFoundException("siteuser not found");
        }
    }


    //회원수정
    public SiteUser modify(SiteUser siteUser,String nickname, String email) {
        siteUser.setNickname(nickname);
        siteUser.setEmail(email);
        this.userRepository.save(user);
        return siteUser;
    }
}

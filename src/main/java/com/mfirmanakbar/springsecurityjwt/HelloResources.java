package com.mfirmanakbar.springsecurityjwt;

import com.mfirmanakbar.springsecurityjwt.models.AuthenticationRequest;
import com.mfirmanakbar.springsecurityjwt.models.AuthenticationResponse;
import com.mfirmanakbar.springsecurityjwt.services.MyUserDetailsService;
import com.mfirmanakbar.springsecurityjwt.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResources {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtTokenUtil;

  @RequestMapping("/hello")
  public String hello() {
    return "Hello hi";
  }

  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(
    @RequestBody AuthenticationRequest authenticationRequest
  ) throws Exception {

    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          authenticationRequest.getUsername(),
          authenticationRequest.getPassword()
        )
      );
    } catch (BadCredentialsException e) {
      throw new Exception("incorrect username or password: ", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getPassword());
    final String jwt = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

}

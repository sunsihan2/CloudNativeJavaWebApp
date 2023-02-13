package com.example.healthcheckapi.controller;

import com.example.healthcheckapi.model.Image;
import com.example.healthcheckapi.model.User;
import com.example.healthcheckapi.repository.ImageRepository;
import com.example.healthcheckapi.repository.UserRepository;
import com.example.healthcheckapi.service.ImageStorageService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageStorageService service;

    public UserController() {
        System.out.println("In User Controller Constructor");
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {

            System.out.println("In post /user");

            if(user==null || user.getPassword() == null || user.getFirst_name() == null ||
                    user.getUsername() == null || user.getLast_name() == null)
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // check if already exists

            System.out.println("calling get user");

            System.out.println("Setting for post request");
            Optional<User> u = userRepository.findByUsername(user.getUsername());

            System.out.println("checking if user is present");
            if (u.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // encrypt password
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);
            System.out.println("encoded: " + encodedPassword);

            User _user = userRepository
                    .save(new User(user.getFirst_name(), user.getLast_name(), user.getPassword(), user.getUsername()));


            System.out.println("user saved in db");

            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("exception: " +e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/self")
    public ResponseEntity<User> getUserByEmail(HttpServletRequest request) {

        try{
            String upd = request.getHeader("authorization");
            if (upd == null || upd.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String pair = new String(Base64.decodeBase64(upd.substring(6)));
            String userName = pair.split(":")[0];
            String password = pair.split(":")[1];

            System.out.println("username: " + userName);
            System.out.println("password: " + password);


            System.out.println("In get /user/self");
            Optional<User> inputUser = userRepository.findByUsername(userName);

            if (inputUser.isPresent()) {
                if (bCryptPasswordEncoder.matches(password, inputUser.get().getPassword())) {

                    return new ResponseEntity<>(inputUser.get(), HttpStatus.OK);
                } else {
                    System.out.println("Password does not match");
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                System.out.println("User Not Found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception:"+e);
        }
        System.out.println("End - User Not Found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/user/self")
    public ResponseEntity<String> updateUser(@RequestBody User user, HttpServletRequest request) {

        System.out.println("In put /user/self");

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if ((user.getFirst_name() == null || user.getFirst_name().isEmpty())
                && (user.getLast_name() == null || user.getLast_name().isEmpty())
                && (user.getPassword() == null || user.getPassword().isEmpty())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.getUsername() != null || user.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String upd = request.getHeader("authorization");
        if (upd == null || upd.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String pair = new String(Base64.decodeBase64(upd.substring(6)));
        String userName = pair.split(":")[0];
        String password = pair.split(":")[1];

        System.out.println("username: " + userName);
        System.out.println("password: " + password);
        
        Optional<User> inputUser = userRepository.findByUsername(userName);
        

        // validate password
        if (inputUser.isPresent()) {
            if (bCryptPasswordEncoder.matches(password, inputUser.get().getPassword())) {// update

                User updatedUser = inputUser.get();
                if(user.getFirst_name() != null)
                    updatedUser.setFirst_name(user.getFirst_name());
                if(user.getLast_name() != null)
                    updatedUser.setLast_name(user.getLast_name());
                if(user.getPassword() != null)
                    updatedUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

                updatedUser.setAccount_updated(OffsetDateTime.now(Clock.systemUTC()).toString());
                
                userRepository.save(updatedUser);
                
                return new ResponseEntity<>("Update success", HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/user/self/pic")
    public ResponseEntity<Image> createImage(@RequestParam(value="profilePic") MultipartFile profilePic, HttpServletRequest request) {

        System.out.println("In post /user/self/pic");
        //check user credentials and get userid
        String upd = request.getHeader("authorization");
        if (upd == null || upd.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String pair = new String(Base64.decodeBase64(upd.substring(6)));
        String userName = pair.split(":")[0];
        String password = pair.split(":")[1];

        Optional<User> inputUser = userRepository.findByUsername(userName);
        Image img;
        if (inputUser.isPresent()) {
            if (bCryptPasswordEncoder.matches(password, inputUser.get().getPassword())) {

                //matches password complete-- add code here
                User user = inputUser.get();

                System.out.println("File Content Type: " + profilePic.getContentType());
                if(!profilePic.getContentType().startsWith("image/"))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

                //check if already image i.e. update request
                Optional<Image> img1 = imageRepository.findByUserId(user.getId());
                if(img1.isPresent())
                {
                    String result = service.deleteFileFromS3Bucket(img1.get().getUrl(), user.getId());
                    imageRepository.delete(img1.get());
                    System.out.println("previous image deleted");
                }
                
                String bucket_name = service.uploadFile( user.getId() + "/" + profilePic.getOriginalFilename(), profilePic);

                String url = bucket_name + "/" + user.getId() + "/" + profilePic.getOriginalFilename();

                img = new Image(profilePic.getOriginalFilename(), user.getId(), url);
                imageRepository.save(img);

            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(img, HttpStatus.CREATED);
    }

    @GetMapping(value = "/user/self/pic")
    public ResponseEntity<Image> getImage(HttpServletRequest request) {

        System.out.println("In get /user/self/pic");

        //check user credentials and get userid
        String upd = request.getHeader("authorization");
        if (upd == null || upd.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String pair = new String(Base64.decodeBase64(upd.substring(6)));
        String userName = pair.split(":")[0];
        String password = pair.split(":")[1];

        System.out.println("username: " + userName);
        System.out.println("password: " + password);

        Optional<User> inputUser = userRepository.findByUsername(userName);
        Optional<Image> img;
        if (inputUser.isPresent()) {

            if (bCryptPasswordEncoder.matches(password, inputUser.get().getPassword())) {

                User user = inputUser.get();
                img = imageRepository.findByUserId(user.getId());
                if (img.isPresent()) {
                    return new ResponseEntity<>(img.get(), HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/user/self/pic")
    public ResponseEntity<String> deleteImage(HttpServletRequest request) {

        System.out.println("In delete /user/self/pic");

        //check user credentials and get userid
        String upd = request.getHeader("authorization");
        if (upd == null || upd.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String pair = new String(Base64.decodeBase64(upd.substring(6)));
        String userName = pair.split(":")[0];
        String password = pair.split(":")[1];

        System.out.println("username: " + userName);
        System.out.println("password: " + password);


        Optional<User> inputUser = userRepository.findByUsername(userName);
        Optional<Image> img;
        if (inputUser.isPresent()) {

            if (bCryptPasswordEncoder.matches(password, inputUser.get().getPassword())) {

                User user = inputUser.get();
                img = imageRepository.findByUserId(user.getId());

                if (img.isPresent()) {

                    String result = service.deleteFileFromS3Bucket(img.get().getUrl(),user.getId());
                    imageRepository.delete(img.get());
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

package com.alcor.ril.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/5
 * Time: 08:28
 */
@Controller
@Slf4j
public class RegistrationController {

//    @Autowired
//    private ApplicationEventPublisher eventPublisher;
//
//    private IUserService userService;
//    @Autowired
//    public void setUserService(IUserService userService) {
//        this.userService = userService;
//    }
//
//
//    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
//    public ModelAndView showRegistrationForm() {
//        log.debug("Rendering registration page.");
//        ModelAndView modelAndView = new ModelAndView();
//        final UserDTO userDTO = new UserDTO();
//        modelAndView.addObject("user", userDTO);
//        modelAndView.setViewName("registration");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
//    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final UserDTO userDTO, final HttpServletRequest request, final Errors errors) {
//        log.debug("Registering user account with information: {}", userDTO);
//
//        final SysUser registered = userService.registerNewUserAccount(userDTO);
//        if (registered == null) {
//            return new ModelAndView("registration", "user", userDTO);
//        }
//        return new ModelAndView("successRegister", "user", userDTO);
//    }
//
//
//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public ModelAndView createNewUser(@Valid SysUser user, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView();
//        SysUser userExists = userService.findByUsername(user.getUsername());
//        if (userExists != null) {
//            log.debug("{} already exists!", user.getUsername());
//            bindingResult
//                    .rejectValue("username", "error.user",
//                            "There is already a user registered with the username provided");
//        }
//        if (bindingResult.hasErrors()) {
//            modelAndView.addObject("user", user);
//        } else {
//            userService.saveUser(user);
//            modelAndView.addObject("successMessage", "User has been registered successfully");
//            modelAndView.addObject("user", new SysUser());
//
//        }
//        modelAndView.setViewName("registration");
//        return modelAndView;
//    }

}

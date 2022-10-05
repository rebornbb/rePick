package com.gosari.repick_project.offlineshopping;

import com.gosari.repick_project.question.QuestionForm;
import com.gosari.repick_project.user.SiteUser;
import com.gosari.repick_project.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/offlineshop")
@RequiredArgsConstructor
@Controller
public class OfflineController {

    private final OfflineService offlineService;
    private final UserService userService;
    @GetMapping("/list")
    public String list(Model model,
                       @PageableDefault (page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
                       Pageable pageable) {

        Page<OfflineShop> list = offlineService.getList(pageable);
        int nowPage = list.getPageable().getPageNumber() + 1; /*pageable에서 넘어온 현재페이지를 가지고올수있다*//*0부터시작하니까 +1*/
        int startPage = Math.max(nowPage - 4, 1); /*매개변수로 들어온 두 값을 비교해서 큰값을 반환 (1일때, -3이되면 안되니까)*/
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); /*마지막 페이지를 초과하면안되니까 이것도 min으로 처리*/

        //BoardService에서 만들어준 boardList가 반환되는데, list라는 이름으로 받아서 넘기겠다는 뜻
        model.addAttribute("list" , list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "offlineshop_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        OfflineShop offlineShop = this.offlineService.getOfflineShop(id);
        model.addAttribute("offlineShop", offlineShop);
        return "offlineshop_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String offlineshopCreate(OfflineForm offlineForm) {
        return "offlineshop_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String offlineshopCreate(@Valid OfflineForm offlineForm,
                                    BindingResult bindingResult, Principal principal) {

        //오류가 있는 경우에는 다시 폼을 작성하는 화면을 렌더링
        if (bindingResult.hasErrors()) {
            return "offlineshop_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.offlineService.create( offlineForm.getShopName(),
                                    offlineForm.getShopNumber(),
                                    offlineForm.getShopTime(),
                                    offlineForm.getShopAddress(),
                                    offlineForm.getShopContents(),
                                    siteUser);

        return "redirect:/offlineshop/list"; // 저장후 목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String offlineshopModify(OfflineForm offlineForm, @PathVariable("id") Integer id, Principal principal) {
        OfflineShop offlineShop = this.offlineService.getOfflineShop(id);
        if(!offlineShop.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        offlineForm.setShopName(offlineShop.getShopName());
        offlineForm.setShopNumber(offlineShop.getShopNumber());
        offlineForm.setShopTime(offlineShop.getShopTime());
        offlineForm.setShopAddress(offlineShop.getShopAddress());
        offlineForm.setShopContents(offlineShop.getShopContents());
        return "offlineshop_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String offlineshopModify(@Valid OfflineForm offlineForm, BindingResult bindingResult,
                                    Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "offlineshop_form";
        }
        OfflineShop offlineShop = this.offlineService.getOfflineShop(id);
        if (!offlineShop.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.offlineService.modify(offlineShop, offlineForm.getShopName(), offlineForm.getShopNumber(),
                offlineForm.getShopTime(),offlineForm.getShopAddress(), offlineForm.getShopContents());
        return String.format("redirect:/offlineshop/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
         OfflineShop offlineShop = this.offlineService.getOfflineShop(id);
         if (!offlineShop.getAuthor().getUsername().equals(principal.getName())) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
         }
         this.offlineService.delete(offlineShop);
         return "redirect:/";
    }


}

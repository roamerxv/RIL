MetronicSidebarMenu = function fun_gen_menu(jquery_object, menu_data) {
    fun_gen_content_div(jquery_object, menu_data);
}

function fun_gen_content_div(jquery_object, data) {
    var menu_content_div = $("" +
        "<div " +
        "            id=\"m_ver_menu\"\n" +
        "            class=\"m-aside-menu  m-aside-menu--skin-dark m-aside-menu--submenu-skin-dark m-aside-menu--dropdown \"\n" +
        "            data-menu-vertical=\"false\"\n" +
        "            data-menu-dropdown=\"true\" data-menu-scrollable=\"true\" data-menu-dropdown-timeout=\"0\"\n" +
        "    >");
    var menu_ul = $("<ul class=\"m-menu__nav  m-menu__nav--dropdown-submenu-arrow \" id=\"m_ver_menu\">\n" +
        "</ul>\n");

    fun_gen_menu_item(menu_ul, data);
    menu_ul.appendTo(menu_content_div);
    menu_content_div.appendTo(jquery_object);
}

function fun_gen_menu_item(parent_jquery_obj, data) {
    if (typeof data == undefined || data.length <= 0) {

    } else {
        for (i in data) {
            var menu = data[i];
            if (typeof (menu.children) == undefined || menu.children === null || menu.children.length <= 0) {
                //如果没有子菜单
                var menu_item = fun_gen_leaf_menu(menu);
                if (menu.parent === "0") {
                    menu_item.appendTo(parent_jquery_obj);
                } else {
                    menu_item.appendTo(parent_jquery_obj);
                }

            } else {
                //如果有子菜单
                var menu_item = fun_gen_menu_has_submenu(menu);
                if (menu.parent === "0") {
                    menu_item.appendTo(parent_jquery_obj);
                } else {
                    // menu_item.appendTo(subnav_ul.children("ul")[0]);
                    // subnav_ul.insertAfter(parent_jquery_obj.children("ul.m-m-menu__subnav")[0]);
                }
                fun_gen_menu_item(menu_item.find("ul.m-menu__subnav")[0], menu.children);
            }

        }
    }
}

//生成枝干菜单
function fun_gen_menu_has_submenu(menu) {
    var menu_item = $("" +
        "            <li class=\"m-menu__item  m-menu__item--submenu\" aria-haspopup=\"true\" data-menu-submenu-toggle=\"hover\"\n" +
        "                name=\"menu_system\">\n" +
        "                <a href=\"#\" class=\"m-menu__link m-menu__toggle\">\n" +
        "                    <span class=\"m-menu__item-here \"></span>\n" +
        "                    <i class=\"m-menu__link-icon " + menu.icon + "\"></i>\n" +
        "                    <span class=\"m-menu__link-text\">" +
        "                             " + menu.text + "\n" +
        "                    </span>\n" +
        "                    <i class=\"m-menu__ver-arrow la la-angle-right\"></i>\n" +
        "                </a>\n" +
        "                <div class=\"m-menu__submenu\">" +
        "                    <span class=\"m-menu__arrow\"></span>\n" +
        "                    <ul class=\"m-menu__subnav\"></ul>" +
        "               </div>" +
        "           </li>        "
        )
    ;
    return menu_item;
}

function fun_test() {
    return $(" <li class=\"m-menu__item \" aria-haspopup=\"true\" data-redirect=\"true\">\n" +
        "                                            <a href=\"/systemMenuMaintain\" class=\"m-menu__link \">\n" +
        "                                                <i class=\"m-menu__link-bullet m-menu__link-bullet--dot\">\n" +
        "                                                    <span></span>\n" +
        "                                                </i>\n" +
        "                                                <span class=\"m-menu__link-text\">系统菜单维护</span>\n" +
        "                                            </a>\n" +
        "                                        </li>");

}

//生成叶子菜单
function fun_gen_leaf_menu(menu) {
    var menu_item = $("<li class=\"m-menu__item\" aria-haspopup=\"true\" name=\"menu_system_dashboard\">\n" +
        "                <a href=\"" + contextPath + menu.url + "\"  class=\"m-menu__link \">\n" +
        "                    <i class=\"m-menu__link-icon " + menu.icon + "\"></i>\n" +
        "                    <span class=\"m-menu__link-title\">\n" +
        "                        <span class=\"m-menu__link-wrap\">\n" +
        "                            <span class=\"m-menu__link-text\">\n" +
        "                                 " + menu.text + "\n" +
        "                            </span>\n" +
        "                        </span>\n" +
        "                    </span>\n" +
        "                </a>\n" +
        "            </li>");
    return menu_item;
}



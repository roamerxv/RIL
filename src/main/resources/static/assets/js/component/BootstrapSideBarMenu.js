BootstrapSidebarMenu = function fun_gen_menu(jquery_object, menu_data) {
    fun_gen_content_div(jquery_object, menu_data);
}

function fun_gen_content_div(jquery_object, data) {
    var menu_content_div = $(" <div class=\"nav-side-menu\"><div class=\"menu-list\" /></div>");
    var menu_ul = $("<ul id=\"menu-content\" class=\"menu-content collapse out\">");

    fun_gen_menu_item(menu_ul, data);
    menu_ul.appendTo(menu_content_div.children("div.menu-list")[0]);
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
                    menu_item.appendTo(parent_jquery_obj);
                    // subnav_ul.insertAfter(parent_jquery_obj.children("ul.m-m-menu__subnav")[0]);
                }
                fun_gen_menu_item(menu_item[2], menu.children);
            }

        }
    }
}


//生成枝干菜单
function fun_gen_menu_has_submenu(menu) {
    var menu_item = $("<li data-toggle=\"collapse\" data-target=\"#" + menu.id + "\" class=\"collapsed\">\n" +
        "                <a href=\"#\"><i class=\"" + menu.icon + "\"></i> " + menu.text + " <span class=\"arrow\"></span></a>\n" +
        "            </li>\n" +
        "            <ul class=\"sub-menu collapse \" id=\"" + menu.id + "\" ></ul>");
    return menu_item;
}

//生成叶子菜单
function fun_gen_leaf_menu(menu) {
    var menu_item = $("<li>\n" +
        "                  <a href=\"#\">\n" +
        "                  <i class=\"" + menu.icon + "\"></i> " + menu.text + "\n" +
        "                  </a>\n" +
        "                </li>");
    return menu_item;
}





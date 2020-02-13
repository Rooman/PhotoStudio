function showHideUserMenu(){
    const userButton = document.getElementsByClassName("user-dropdown__link")[0];
    const userMenu = document.getElementsByClassName("user-dropdown__user-menu")[0];

    userButton.addEventListener("click", function(event){
        event.preventDefault();
        userMenu.classList.toggle("show");
    });
}

function main() {
    showHideUserMenu();
}

main();
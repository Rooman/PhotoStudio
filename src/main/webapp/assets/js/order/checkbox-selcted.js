var $table = document.querySelector('.photo-element');

$table.addEventListener("click", function(ev){
  if (ev.target.tagName == "INPUT") {
    if (ev.target.checked) {
      ev.target.parentNode.parentNode.classList.add("selected");
    }else {
      ev.target.parentNode.parentNode.classList.remove("selected");
    }
  }
});
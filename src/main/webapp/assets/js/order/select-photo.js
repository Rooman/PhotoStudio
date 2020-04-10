function selectAll() {
  setCheckboxCheckedValue(true);
  window.document.getElementById('selectedPhotos').value = 'all';
}

function unSelectAll() {
  setCheckboxCheckedValue(false);
  window.document.getElementById('selectedPhotos').value = '';
}

function onClickCheckBox(idPhoto)
{
  let checkbox = document.getElementById('checkbox'+String(idPhoto));
  let selected = document.getElementById('selectedPhotos').value;
  if(checkbox.checked==true) {
     if (selected!='') {
        selected = selected +' , ' + String(idPhoto);
     } else {
       selected = String(idPhoto);
     }
  } else {
     if (selected == String(idPhoto)) {
        selected = '';
     } else {
        selected = selected.replace(String(idPhoto)+ ' , ', '');
        selected = selected.replace(' , ' + String(idPhoto), '');
     }
  }
  document.getElementById('selectedPhotos').value = selected;
}

function setCheckboxCheckedValue(value){
   let x = document.querySelectorAll("input[type='checkbox']");
   let i;
   for (i = 0; i < x.length; i++) {
     if (!x[i].disabled) {
        x[i].checked = value;
      }
   }
}

function fullScreen() {
    const modal = document.getElementById("photoModal");
    const modalImg = document.getElementById("photoModalId");
    const photoCards = document.getElementsByClassName("order-photo-card");

    for (let photoCard of photoCards) {
        const zoomButton = photoCard.getElementsByClassName("zoom__button")[0];
        const img = photoCard.getElementsByTagName("img")[0];
        zoomButton.onclick = function (e) {
            e.preventDefault();
            modal.style.display = "block";
            modalImg.src = img.src;
        }
    }

    const span = document.getElementsByClassName("close")[0];

    span.onclick = function() {
        modal.style.display = "none";
    };
}

fullScreen();
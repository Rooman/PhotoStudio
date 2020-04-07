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
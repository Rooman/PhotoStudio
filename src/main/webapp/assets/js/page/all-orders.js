function showHideFilterTable(element) {
  let srcElement = document.getElementById(element);
	 if (srcElement != null) {
		if (srcElement.style.display == "block") {
			srcElement.style.display = 'none';
		} else {
			srcElement.style.display = 'block';
	}
  }
}

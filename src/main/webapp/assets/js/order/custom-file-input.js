let inputs = document.querySelectorAll('.inputfile');

inputs.forEach(function (input) {
    let label = input.nextElementSibling,
        labelVal = label.innerHTML;

    input.addEventListener('change', function () {
        let fileName = (this.getAttribute('data-multiple-caption') || '').replace('{count}', this.files.length);

        if (fileName)
            label.querySelector('span').innerHTML = fileName;
        else
            label.innerHTML = labelVal;
    });
    // Firefox bug fix
    input.addEventListener('focus', function () {
        input.classList.add('has-focus');
    });
    input.addEventListener('blur', function () {
        input.classList.remove('has-focus');
    });
});

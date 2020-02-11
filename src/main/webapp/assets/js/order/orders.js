function parameterize() {
    let url = document.location.origin + '/orders';
    let params = {};
    document.querySelectorAll('#parameters .select-selected').forEach((element) => {
        if (element.value.length > 0
        )
            params[element.name] = element.value;
    });

    let e = document.getElementById("inputState");
    let optionState = e.options[e.selectedIndex];
    if (optionState.text.length > 0) {
        params[e.name] = optionState.text;
    }

    let query = Object.keys(params)
        .map(k => k + '=' + params[k]
        ).join('&');
    url += '?' + query;
    document.getElementById("add_parameters").href = url;
}
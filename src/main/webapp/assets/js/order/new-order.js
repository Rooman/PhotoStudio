function createNewOrder() {
    const email = $('#email')[0].value;
    const comment = $('#comment')[0].value;
    const contextPath = $('#contextPath')[0].value;
    console.log(contextPath);
    if (email === "") {
        alert("Email field empty");
    } else {
        $.post({
            url: contextPath + '/order',
            data: {
                    email: email,
                    comment: comment,
                  },
            success: () => location.replace(contextPath + '/orders')

        })
    }
}

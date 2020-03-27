// Delete a user
function deleteUser(id) {
    event.preventDefault();

    let xhr = new XMLHttpRequest();

    xhr.open("DELETE", 'user?id=' + id, true);

    xhr.ontimeout = function () {
        console.log("timeout error");
    };

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location = "admin/users";
        }
    };

    xhr.onerror = function () {
        console.log("error");
        console.log(xhr.response);
        console.log(xhr.responseText);
    };

    xhr.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');
    xhr.send();
}

// Update a user
function updateUser(id) {
    event.preventDefault();

    let xhr = new XMLHttpRequest();

    xhr.open("PUT", 'user?id=' + id, true);

    xhr.ontimeout = function () {
        console.log("timeout error");
    };

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location = "admin/users";
        }
    };

    xhr.onerror = function () {
        console.log("error");
        console.log(xhr.response);
        console.log(xhr.responseText);
    };

    xhr.setRequestHeader('content-type', 'application/json;charset=UTF-8');

    const user = {};
    user.id = id;
    user.email = document.getElementById("email").value;
    user.phoneNumber = document.getElementById("phoneNumber").value;
    user.title = document.getElementById("title").value;
    user.firstName = document.getElementById("firstName").value;
    user.lastName = document.getElementById("lastName").value;
    user.country = document.getElementById("country").value;
    user.city = document.getElementById("city").value;
    user.address = document.getElementById("address").value;
    user.zip = document.getElementById("zip").value;
    user.additionalInfo = document.getElementById("additionalInfo").value;
    user.langId = document.getElementById("langId").value;

    const json = JSON.stringify(user);

    xhr.send(json);
}

//Get user for order
$('.double-click').dblclick(function(e) {
    window.location.href = e.target.parentElement.id;
});
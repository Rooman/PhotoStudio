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
function updateUser(id){

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

    xhr.setRequestHeader('content-type', 'application/x-www-form-urlencoded;charset=UTF-8');

    //let email = document.getElementById("email").value;
    let id = document.getElementById('id').value
   	let email = document.getElementById('email').value
   	xhr.send("id=" + id + "&email=" + email);
   }
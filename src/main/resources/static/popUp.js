function openPopUp(employeeId) {
    document.getElementById('hidden-employee-id').value = employeeId;
    document.getElementById("delete-employee-pop-up").style.display = "block";
}

function closePopUp() {
    document.getElementById("delete-employee-pop-up").style.display = "none";
}

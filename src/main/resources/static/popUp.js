function openPopUp(employeeId , employeeName) {
    document.getElementById('hidden-employee-id').value = employeeId;
    document.getElementById('employee-name').textContent = employeeName;
    document.getElementById("delete-employee-pop-up").style.display = "block";
}

function closePopUp() {
    document.getElementById("delete-employee-pop-up").style.display = "none";
}

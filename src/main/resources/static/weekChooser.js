 document.getElementById('weekChooser').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.submit();
        }
    });

    function changeWeek(offset) {
        const weekInput = document.getElementById('weekNumber');
        let weekNumber = parseInt(weekInput.value, 10);

        weekNumber += offset;

        if (weekNumber < 1) {
            weekNumber = 1;
        }

        weekInput.value = weekNumber;
        document.getElementById('weekChooser').submit();
    }
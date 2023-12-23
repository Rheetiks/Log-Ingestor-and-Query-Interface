function performSearch() {
    var selectedValue = getSelectedRadioValue('queryType');
    let text=document.getElementById('additionalInput').value
    document.getElementById('table-cont').style.display='block';
    let log={};
    log.filterText=text;

    let url='http://localhost:3000/api/logs/textBasedSearch';

    if(selectedValue=='filter'){
        log.filterType=document.getElementById('filters').value;
        url='http://localhost:3000/api/logs/filterBasedSearch';
    } else{
        log.textSearch=document.getElementById('additionalInput').value;
    }
    console.log(log)
        
    fetch(url, {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json'
        },
        body: JSON.stringify(log),
    })        .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json(); // Parse the response body as JSON
      })
      .then(data => {
        // Handle the data returned by the API
        console.log('API response:', data);
        addToTable(data)
      })
      .catch(error => {
        // Handle errors during the request
        console.error('Error:', error);
      });
                

    
}



function addToTable(res){
    let table=document.getElementById('table-body');
    table.innerHTML="";
        res.map(log=>{
            table.innerHTML+=
            `<tr>
                <td scope="col">${log.level}</td>
                <td scope="col">${log.message}</td>
                <td scope="col">${log.resourceId}</td>
                <td scope="col">${log.timestamp}</td>
                <td scope="col">${log.traceId}</td>
                <td scope="col">${log.spanId}</td>
                <td scope="col">${log.commit}</td>
                <td scope="col">${JSON.stringify(log.metadata)}</td>
            </tr>`
        }).join('');
}


function getSelectedRadioValue(name) {
    var radios = document.getElementsByName(name);

    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            return radios[i].value;
        }
    }

}
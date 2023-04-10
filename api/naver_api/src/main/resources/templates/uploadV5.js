//수정시, binding result시 동일하게

//작성시 파일을 선택하면 파일들을 보여준다
//수정시 파일의 목록들을 보여주고 삭제가 가능하다
//binding result시 기존 데이터는 그대로(파일을 그대로 넘긴다. 타임리크로 넘어오는걸 받아오자)
var selectedFiles = [];
//델피뇸
//스토크
//파일이 대용량일 경우 비동기로 보내야한다.
function uploadFile() {
    console.log("Test");
    var formData = new FormData($("#myForm")[0]);
    $.ajax({
        url: "/uploadTest",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            console.log(formData);
            console.log(response);
        },
        error: function (xhr, status, error) {
            console.log(error);
        }
    });
}

document.querySelector('#file-upload').addEventListener('change', function () {
    addFileNames();
});

function addFileNames() {
    var fileNames = document.querySelector('#file-upload').files;
    var fileNamesDiv = document.querySelector('.file-names');
    fileNamesDiv.innerHTML = "";
    for (var i = 0; i < fileNames.length; i++) {
        var fileName = fileNames[i].name;
        var fileId = "file-" + i;
        var fileDiv = document.createElement("div");
        fileDiv.classList.add("file-div");

        var nameSpan = document.createElement("span");
        nameSpan.innerText = fileName;
        fileDiv.appendChild(nameSpan);

        var deleteBtn = document.createElement("button");
        deleteBtn.classList.add("delete-btn");
        deleteBtn.innerText = "Delete";
        deleteBtn.setAttribute("data-file-id", fileId);
        deleteBtn.addEventListener("click", deleteFile);
        fileDiv.appendChild(deleteBtn);

        fileDiv.setAttribute("id", fileId);
        fileNamesDiv.appendChild(fileDiv);

        // selectedFiles 배열에 추가
        selectedFiles.push(fileNames[i]);
    }
    // input 엘리먼트 초기화(input은 동일한 문서를 두번 허용하지 않기 때문에 비워줌)
    document.querySelector('#file-upload').value = "";
}

function deleteFile() {
    var fileId = this.getAttribute("data-file-id");
    var fileDiv = document.getElementById(fileId);

    // selectedFiles 배열에서 삭제
    selectedFiles = selectedFiles.filter(function(file) {
        console.log(file.name, document.getElementById(fileId).querySelector('span').textContent);
        return file.name !== document.getElementById(fileId).querySelector('span').textContent;
    });

    fileDiv.parentNode.removeChild(fileDiv);
    printSelectedFiles();
}

function printSelectedFiles() {
    console.log(selectedFiles);
}

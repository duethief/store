<div>
    <h3>freemarker example code</h3>
    <table>
        <thead>
        <tr>
            <th>Title</th>
            <th>status</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <#list books as book>
        <tr>
            <td>${book.name}</td>
            <#if book.status == "CanRent">
            <td>일반</td>
            <#elseif book.status == "RentNow" >
            <td>대여중</td>
            <#else>
            <td>분실</td>
            </#if>
            <td>${book.comment}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
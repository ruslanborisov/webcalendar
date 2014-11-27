<div id="searchBlock" class="border-bottom">
    <label for="searchSelect" style="margin: 0 0 10px 10px">Search by:</label>
    <select id="searchSelect" class="input" name="search" style="width: 155px;">
        <option value="searchByTitle">Title</option>
        <option value="searchByTitleStartWith">Title start with..</option>
        <option value="searchByDate">Date</option>
        <option value="searchByAttender">Attender</option>
        <option value="searchIntoPeriod">Period</option>
        <option value="searchByAttenderIntoPeriod">Period (Attender)</option>
    </select>
</div>

<form id="formSearch">
    <p> <input id="title" class="input" type="text" placeholder="Enter title.." name="title"> </p>
    <p> <input id="date" class="input dispNone"  type="text" placeholder="Enter date.." name="date"> </p>
    <p> <input id="startDate" class="input dispNone" type="text" placeholder="Enter Start Date.." name="startDate" readonly> </p>
    <p> <input id="endDate" class="input dispNone" type="text" placeholder="Enter End Date.." name="endDate" readonly> </p>
    <p> <input class="input dispNone" id="attender" type="text" placeholder="Enter email.." name="name"> </p>
    <div id="exceptionSearch" style="margin: 5px" class="exception dispNone"></div>
    <p><input id="submitSearch" class="button" type="submit" value="FIND"></p>
</form>

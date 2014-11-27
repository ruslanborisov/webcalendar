<div id="searchBlock" class="border-bottom">
    <label for="searchSelect" style="margin: 0 0 10px 10px">Search by:</label>
    <select id="searchSelect" class="input" name="search">
        <option value="searchFreeTime" selected>Time</option>
        <option value="isAttenderFree">Attender</option>
    </select>
</div>

<form id="formSearchFree">
    <p> <input id="startDate" class="input"  type="text" placeholder="Enter Start Date.." name="startDate" maxlength="10"> </p>
    <p> <input id="startTime" class="input" type="text" placeholder="Start Time.." name="startTime" maxlength="10" style="width :90px"> </p>
    <p> <input id="endDate" class="input"  type="text" placeholder="Enter End Date.." name="endDate" maxlength="10"> </p>
    <p> <input id="endTime" class="input" type="text" placeholder="End Time.." name="endTime" maxlength="10" style="width :90px"> </p>
    <p> <input class="input dispNone" id="attender" type="text" placeholder="Enter email.." name="name" maxlength="50"> </p>
    <div id="exceptionSearch" style="margin: 5px" class="exception dispNone"></div>
    <p><input id="submitSearchFree" class="button" type="submit" value="FIND"></p>
    <p class="infoFont"> <span class="metaFont">Note:</span> "All Day" events is not involved in the search</p>
</form>
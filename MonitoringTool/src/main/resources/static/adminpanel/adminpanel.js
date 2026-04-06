/**
 * Krushi Vikas Dashboard Logic
 * Handles grouping activities by Project Name and rendering nested views with CSV export
 */

const TODAY = new Date('2026-03-11');
let rawData = [];
let groupedData = {};

window.addEventListener('DOMContentLoaded', () => {

    loadDashboardFromBackend();

    document.getElementById('searchInput').oninput = render;

    const uploadBtn = document.getElementById('uploadBtn');
    const fileInput = document.getElementById('excelFileInput');

    uploadBtn.onclick = () => fileInput.click();

    // ⭐ THIS WAS MISSING
    fileInput.addEventListener("change", uploadExcelToBackend);

    document.getElementById('newProjectBtn').onclick = () => {
        resetForm();
        document.getElementById('modal-title').innerText = "New Project Activity";
        document.getElementById('editModal').style.display = 'flex';
    };

    document.getElementById('saveDataBtn').onclick = saveProject;

    document.getElementById('logoutBtn').onclick = () => {
        showToast("Logging out...");
        setTimeout(() => {
            window.location.href = "/logout";
        }, 800);
    };

});

function uploadExcelToBackend(e) {

    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    showToast("Uploading Excel...");

    fetch("/api/admin/upload-excel", {
        method: "POST",
        body: formData
    })
    .then(res => {
        if(!res.ok){
            throw new Error("Upload failed");
        }
        return res.json();
    })
    .then(result => {

        console.log("Upload result:", result);

        const success = result.success || 0;
        const failed = result.failed || 0;

        showToast(`Imported ${success} activities, Failed ${failed}`);

        loadDashboardFromBackend();

    })
    .catch(err => {
        console.error(err);
        showToast("Excel upload failed");
    });
}


window.updateInline = function(id, field, value) {

    const p = rawData.find(x => String(x.id) === String(id));

    if(!p) return;

    if(field === "status"){

        fetch(`/api/admin/activity/${id}/status?status=${value}`,{
            method:"POST"
        })
        .then(res=>res.text())
        .then(msg=>showToast(msg))
        .catch(()=>showToast("Status update failed"));

    }

    if(field === "ach"){
        p.ach = value;
    }
    else if(field === "target"){
        p.target = value;
    }
    else{
        p[field] = value;
    }

    render();
}

/**
 * Fetch data and group by Project Name
 */
async function loadDashboardFromBackend() {
    try {
        const res = await fetch("/api/admin/dashboard");
        if (!res.ok) throw new Error("Data fetch failed");
        
        const json = await res.json();
        rawData = json.map(a => ({
            id: String(a.id),
            project: a.projectName?.trim() || 'NO PROJECT NAME',
            activity: a.activityName || 'General Task',
            start: a.startDate || '',
            end: a.endDate || '',
            target: a.target || 0,
            ach: a.achievement || 0,
            status: a.status || 'To Be Planned',
            resp: a.responsibleEmail || '',
            mgr: a.managerEmail || '',
            coord: a.coordinatorEmail || ''
        }));
        
        groupData();
        render();
    } catch (e) { 
        console.error("Dashboard Load Error:", e);
        rawData = [];
        groupedData = {};
        render();
    }
}

/**
 * Groups raw activity data by Project Name
 */
function groupData() {
    groupedData = {};
    rawData.forEach(item => {
        if (!groupedData[item.project]) {
            groupedData[item.project] = [];
        }
        groupedData[item.project].push(item);
    });
}

/**
 * Renders the grouped table
 */
function render() {
    const tableBody = document.getElementById('projectTable');
    const query = document.getElementById('searchInput').value.toLowerCase();
    
    const projectNames = Object.keys(groupedData).filter(name => 
        name.toLowerCase().includes(query)
    );

    if (projectNames.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding:20px;">No projects found.</td></tr>`;
        return;
    }

    tableBody.innerHTML = projectNames.map((projName, index) => {
        const activities = groupedData[projName];
        const activityCount = activities.length;
        const projId = `proj-${index}`;

        const resps = [...new Set(activities.map(a => a.resp))].filter(Boolean).join(', ');
        const mgrs = [...new Set(activities.map(a => a.mgr))].filter(Boolean).join(', ');

        return `
        <tr class="project-row" onclick="toggleDetails('${projId}')" style="background: #fdfdfd; border-left: 4px solid var(--kv-blue);">
            <td class="col-project">
                <div class="proj-name">
                    <i data-lucide="chevron-right" id="icon-${projId}" style="width:16px; transition: transform 0.2s;"></i>
                    <span style="font-size: 1rem;">${projName}</span>
                    <span class="overdue-badge" style="background: #e2e8f0; color: #475569; margin-left:8px;">${activityCount} Activities</span>
                </div>
            </td>
            <td class="col-stakeholders">
                <div class="sh-row"><span class="sh-label">RESP:</span> ${resps || '---'}</div>
                <div class="sh-row"><span class="sh-label">MGR:</span> ${mgrs || '---'}</div>
            </td>
            <td class="col-status">
                <span class="status-badge" style="background: #f1f5f9; color: #64748b; border: 1px solid #e2e8f0;">Project View</span>
            </td>
            <td class="col-actions">
                 <div style="display: flex; gap: 8px; justify-content: flex-end;">
                    <button class="btn-top btn-top-warning" style="padding:6px 10px; font-size: 0.7rem;" onclick="event.stopPropagation(); downloadCSV('${projName}')">
                        <i data-lucide="download" style="width:12px"></i> CSV
                    </button>
                    <button class="btn-top" style="padding:6px 10px; font-size: 0.7rem;" onclick="event.stopPropagation(); addNewActivityToProject('${projName}')">
                        <i data-lucide="plus" style="width:12px"></i> Add
                    </button>
                 </div>
            </td>
        </tr>
        <tr class="detail-row" id="detail-${projId}">
            <td colspan="4" style="padding: 0; background: #fff;">
                <div style="padding: 15px 15px 15px 45px; border-bottom: 2px solid #f1f5f9;">
                    <table style="width: 100%; font-size: 0.8rem; border: 1px solid #edf2f7; border-radius: 8px; overflow: hidden;">
                        <thead style="background: #f8fafc;">
                            <tr>
                                <th style="padding: 10px;">Activity Name</th>
                                <th style="padding: 10px;">Timeline</th>
                                <th style="padding: 10px;">Target / Ach</th>
                                <th style="padding: 10px;">Status</th>
                                <th style="padding: 10px; text-align:right;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${activities.map(a => {
                                const isOverdue = a.status !== 'Complete' && a.end && new Date(a.end) < TODAY;
                                let sCls = 'st-tobeplanned';
                                if (a.status === 'Complete') sCls = 'st-complete';
                                else if (isOverdue) sCls = 'st-overdue-select';
                                else if (a.status === 'In Progress') sCls = 'st-inprogress';
                                else if (a.status === 'Planned') sCls = 'st-planned';

                                return `
                                <tr>
                                    <td style="padding: 10px; font-weight: 600;">${a.activity}</td>
                                    <td style="padding: 10px;">
                                        ${a.start || 'N/A'} - ${a.end || 'N/A'}
                                        ${isOverdue ? '<br><span class="overdue-badge">OVERDUE</span>' : ''}
                                    </td>
                                    <td style="padding: 10px;">${a.target} / ${a.ach}</td>
                                    <td style="padding: 10px; width: 140px;">
                                        <span class="status-badge ${sCls}">${a.status}</span>
                                    </td>
                                    <td style="padding: 10px; text-align:right;">
                                        <button class="btn-top" style="padding:4px 8px; font-size: 0.65rem; border: 1px solid #ddd; display: inline-flex;" onclick="editRow('${a.id}')">Edit</button>
                                    </td>
                                </tr>
                                `;
                            }).join('')}
                        </tbody>
                    </table>
                </div>
            </td>
        </tr>`;
    }).join('');
    
    if (typeof lucide !== 'undefined') lucide.createIcons();
    updateStats();
}

/**
 * Generates and downloads a CSV for a specific project
 */
function downloadCSV(projName) {
    const activities = groupedData[projName];
    if (!activities || activities.length === 0) return;

    const headers = ["Project Name", "Activity Name", "Start Date", "End Date", "Target", "Achievement", "Status", "Responsible", "Manager", "Coordinator"];
    const rows = activities.map(a => [
        `"${a.project}"`,
        `"${a.activity}"`,
        `"${a.start}"`,
        `"${a.end}"`,
        a.target,
        a.ach,
        `"${a.status}"`,
        `"${a.resp}"`,
        `"${a.mgr}"`,
        `"${a.coord}"`
    ]);

    const csvContent = [headers.join(","), ...rows.map(r => r.join(","))].join("\n");
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", `${projName.replace(/\s+/g, '_')}_Report.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast(`CSV downloaded for ${projName}`);
}

/**
 * Expand/Collapse Project Details
 */
function toggleDetails(id) {
    const row = document.getElementById(`detail-${id}`);
    const icon = document.getElementById(`icon-${id}`);
    if (!row) return;

    const isActive = row.classList.contains('active');
    row.classList.toggle('active');
    icon.style.transform = isActive ? 'rotate(0deg)' : 'rotate(90deg)';
}

/**
 * Pre-fills project name for a new activity
 */
function addNewActivityToProject(projName) {
    resetForm();
    document.getElementById('edit-project').value = projName;
    document.getElementById('modal-title').innerText = `Add Activity to ${projName}`;
    document.getElementById('editModal').style.display = 'flex';
}

/**
 * Edit Activity - Fill Modal
 */
function editRow(id) {
    const p = rawData.find(x => x.id === id);
    if (!p) return;

    document.getElementById('modal-title').innerText = "Edit Activity Details";
    document.getElementById('edit-id').value = p.id;
    document.getElementById('edit-project').value = p.project;
    document.getElementById('edit-activity').value = p.activity;
    document.getElementById('edit-start').value = p.start;
    document.getElementById('edit-end').value = p.end;
    document.getElementById('edit-target').value = p.target;
    document.getElementById('edit-ach').value = p.ach;
    document.getElementById('edit-status').value = p.status;
    document.getElementById('edit-resp').value = p.resp;
    document.getElementById('edit-mgr').value = p.mgr;
    document.getElementById('edit-coord').value = p.coord;
    
    document.getElementById('editModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('editModal').style.display = 'none';
}

function resetForm() {
    document.getElementById('edit-id').value = '';
    document.querySelectorAll('.form-control').forEach(el => el.value = '');
    document.getElementById('edit-status').value = 'To Be Planned';
}

/**
 * Save Activity (Create or Update)
 */
async function saveProject() {
    const id = document.getElementById('edit-id').value;
    const entry = {
        projectName: document.getElementById('edit-project').value,
        activityName: document.getElementById('edit-activity').value,
        startDate: document.getElementById('edit-start').value,
        endDate: document.getElementById('edit-end').value,
        target: parseInt(document.getElementById('edit-target').value) || 0,
        achievement: parseInt(document.getElementById('edit-ach').value) || 0,
        status: document.getElementById('edit-status').value,
        responsibleEmail: document.getElementById('edit-resp').value,
        managerEmail: document.getElementById('edit-mgr').value,
        coordinatorEmail: document.getElementById('edit-coord').value
    };

    if (!entry.projectName || !entry.activityName) {
        showToast("Project and Activity name are required");
        return;
    }

    const url = id ? `/api/admin/activity/${id}` : "/api/admin/activity";
    try {
        const response = await fetch(url, {
            method: id ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(entry)
        });

        if (response.ok) {
            showToast(id ? "Activity Updated" : "Activity Added");
            closeModal();
            loadDashboardFromBackend();
        }
    } catch (e) {
        showToast("Error saving data");
    }
}

/**
 * UI Helpers
 */
function showToast(msg) {
    const t = document.getElementById('toast');
    if (!t) return;
    t.innerText = msg; t.style.display = 'block';
    setTimeout(() => t.style.display = 'none', 3000);
}

function updateStats() {
    document.getElementById('stat-total').innerText = rawData.length;
    document.getElementById('stat-inprogress').innerText = rawData.filter(p => p.status === 'In Progress').length;
    document.getElementById('stat-completed').innerText = rawData.filter(p => p.status === 'Complete').length;
    document.getElementById('stat-overdue').innerText = rawData.filter(p => p.status !== 'Complete' && p.end && new Date(p.end) < TODAY).length;
}
let rawData = [];
const baseProject = "10K FPO Promotion-Gujrat";
const statuses = ["In Progress", "Complete", "PLANNED", "TO_BE_PLANNED"];

// Initialize Mock Data
function initData() {
    rawData.push(
        { id: 1, projectName: baseProject, activityName: "Payment Note for Exposure to BOD/CEO/ Accountant", startDate: "N/A", endDate: "N/A", target: 0, achievement: 0, status: "In Progress", responsible: "dynogawade@gmail.com" },
        { id: 2, projectName: baseProject, activityName: "Farmer Mobilization - Uchal FPO", startDate: "N/A", endDate: "N/A", target: 0, achievement: 0, status: "Complete", responsible: "dynogawade@gmail.com" },
        { id: 3, projectName: baseProject, activityName: "Business Plan preparation-50% business execution", startDate: "1/24/2026", endDate: "1/30/2026", target: 10, achievement: 2, status: "PLANNED", responsible: "ravirajjagtap1186@gmail.com" },
        { id: 4, projectName: baseProject, activityName: "Pre-Approval Note for Stake holder Board", startDate: "1/23/2026", endDate: "1/29/2026", target: 5, achievement: 0, status: "TO_BE_PLANNED", responsible: "patilrushikesh1983@gmail.com" },
        { id: 5, projectName: "Other Regional Project", activityName: "Demo Regional Task", startDate: "1/25/2026", endDate: "1/30/2026", target: 0, achievement: 0, status: "TO_BE_PLANNED", responsible: "dynogawade@gmail.com" }
    );

    // 68 tasks generate karna
    for (let i = 6; i <= 68; i++) {
        rawData.push({
            id: i,
            projectName: i % 10 === 0 ? "New Initiative Project" : baseProject,
            activityName: `Automatic Activity Task #${i}`,
            startDate: "1/10/2026",
            endDate: "2/15/2026",
            target: 100,
            achievement: 45,
            status: statuses[Math.floor(Math.random() * statuses.length)],
            responsible: "team@krushivikas.com"
        });
    }
}

let currentProject = "";
let currentActivityId = null;

document.addEventListener('DOMContentLoaded', () => {
    initData();
    updateStats();
    renderProjects();
    setupUserMenu();
    lucide.createIcons();
    setupSearch();
});

// User profile dropdown toggle setup
function setupUserMenu() {
    const userToggle = document.getElementById('userToggle');
    const userDropdown = document.getElementById('userDropdown');

    if (userToggle && userDropdown) {
        userToggle.addEventListener('click', (e) => {
            e.stopPropagation();
            userDropdown.classList.toggle('hidden');
        });

        document.addEventListener('click', () => {
            userDropdown.classList.add('hidden');
        });
    }
}

/**
 * Logout Functionality
 * Isse user aapke banaye huye login form par redirect ho jayega.
 */
window.handleLogout = () => {
    // Notification dikhana
    showToast("");
    
    setTimeout(() => {
        // Saara session data aur local storage clear karna
        localStorage.clear();
        sessionStorage.clear();
        
        // redirect to your login form page
        // Note: 'login.html' ko apne login file name se replace karein (e.g., 'index.html' or 'auth.html')
        window.location.href = "/logout"; 
    }, 1500);
};

// Dashboard statistics update karna
function updateStats() {
    const total = rawData.length;
    const progress = rawData.filter(d => d.status === "In Progress").length;
    const complete = rawData.filter(d => d.status === "Complete").length;
    
    const today = new Date();
    const overdue = rawData.filter(d => {
        if (!d.endDate || d.endDate === "N/A" || d.status === "Complete") return false;
        return new Date(d.endDate) < today;
    }).length;

    document.getElementById('stat-total').innerText = total;
    document.getElementById('stat-progress').innerText = progress;
    document.getElementById('stat-complete').innerText = complete;
    document.getElementById('stat-overdue').innerText = overdue;
}

// Project list table render karna
function renderProjects() {
    const projects = {};
    rawData.forEach(item => {
        if (!projects[item.projectName]) {
            projects[item.projectName] = { name: item.projectName, count: 0, users: new Set() };
        }
        projects[item.projectName].count++;
        projects[item.projectName].users.add(item.responsible);
    });

    const tbody = document.getElementById('projectListBody');
    if (tbody) {
        tbody.innerHTML = Object.values(projects).map(p => `
            <tr>
                <td style="font-weight:700; color:var(--kv-blue)">${p.name}</td>
                <td><div style="font-size:0.7rem; color:var(--text-muted)">${Array.from(p.users).slice(0, 2).join(', ')}...</div></td>
                <td><b>${p.count}</b> Tasks</td>
                <td>
                    <button class="btn-action btn-edit" onclick="showActivities('${p.name.replace(/'/g, "\\'")}')">
                        <i data-lucide="list" size="14"></i> View Activities
                    </button>
                </td>
            </tr>
        `).join('');
        lucide.createIcons();
    }
}

// Specific project ki activities dikhana
window.showActivities = (projectName) => {
    if (projectName) currentProject = projectName;
    document.getElementById('selectedProjectTitle').innerText = currentProject;
    
    const activities = rawData.filter(item => item.projectName === currentProject);
    const tbody = document.getElementById('activityListBody');
    
    if (tbody) {
        tbody.innerHTML = activities.map(a => `
            <tr>
                <td>${a.activityName}</td>
                <td><span class="status-pill status-${a.status.toLowerCase().replace(/ /g, '')}">${a.status}</span></td>
                <td>${a.endDate}</td>
                <td>
                    <button class="btn-action" onclick="editActivity(${a.id})">
                        <i data-lucide="chevron-right" size="12"></i> Update
                    </button>
                </td>
            </tr>
        `).join('');
        
        toggleView('activities-view');
        document.getElementById('stats-section').classList.add('hidden');
        lucide.createIcons();
        window.scrollTo(0, 0);
    }
};

window.showProjects = () => {
    toggleView('project-list-view');
    document.getElementById('stats-section').classList.remove('hidden');
    updateStats();
};

window.editActivity = (id) => {
    const a = rawData.find(x => x.id === id);
    if (!a) return;
    currentActivityId = id;

    document.getElementById('f_proj').value = a.projectName;
    document.getElementById('f_act').value = a.activityName;
    document.getElementById('f_end').value = a.endDate;
    document.getElementById('f_status').value = a.status;
    document.getElementById('f_target').value = a.target;
    document.getElementById('f_ach').value = a.achievement;
    document.getElementById('f_resp').value = a.responsible;

    toggleView('edit-activity-view');
};

window.updateActivityStatus = () => {
    const newStatus = document.getElementById('f_status').value;
    const item = rawData.find(x => x.id === currentActivityId);
    if (item) {
        item.status = newStatus;
        showActivities(); 
        showToast("Status update ho gaya!");
    }
};

function toggleView(viewId) {
    const views = ['project-list-view', 'activities-view', 'edit-activity-view'];
    views.forEach(v => {
        const el = document.getElementById(v);
        if (el) el.classList.add('hidden');
    });
    const target = document.getElementById(viewId);
    if (target) target.classList.remove('hidden');
}

function setupSearch() {
    const searchInput = document.getElementById('projectSearch');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const val = e.target.value.toLowerCase();
            document.querySelectorAll('#projectListBody tr').forEach(tr => {
                tr.style.display = tr.innerText.toLowerCase().includes(val) ? '' : 'none';
            });
        });
    }
}

function showToast(msg) {
    const t = document.getElementById('toast');
    if (t) {
        t.innerText = msg;
        t.style.display = 'block';
        setTimeout(() => t.style.display = 'none', 2500);
    }
}
const API_BASE = "http://localhost:8081/api";

const state = {
  token: localStorage.getItem("campusTradeToken") || "",
  user: JSON.parse(localStorage.getItem("campusTradeUser") || "null"),
  activeTab: "market"
};

const categoryNames = {
  Books: "图书教材",
  Electronics: "数码电器",
  "Daily Goods": "生活用品",
  Sports: "运动户外",
  Clothing: "服饰鞋包",
  Other: "其他"
};

const statusNames = {
  DRAFT: "草稿",
  ON_SALE: "在售",
  LOCKED: "交易中",
  SOLD: "已售出",
  OFF_SHELF: "已下架",
  PENDING: "待卖家确认",
  CONFIRMED: "已确认",
  COMPLETED: "已完成",
  CANCELLED: "已取消"
};

const roleNames = {
  STUDENT: "学生",
  ADMIN: "管理员"
};

const displayNames = {
  Alice: "小艾",
  Bob: "小博",
  Admin: "管理员"
};

const accountNames = {
  alice: "学生账号一",
  bob: "学生账号二",
  admin: "管理员账号"
};

const campusNames = {
  "Main Campus": "主校区",
  "North Campus": "北校区"
};

const conditionNames = {
  Good: "成色良好",
  "Almost New": "几乎全新",
  Used: "正常使用痕迹"
};

const locationNames = {
  "Library Gate": "图书馆门口",
  "Dormitory Building 3": "三号宿舍楼",
  "Canteen Entrance": "食堂门口",
  "Sports Field": "操场"
};

const seededProductNames = {
  "Mechanical Keyboard 87 Keys": "87键机械键盘",
  "Data Structures Textbook": "数据结构教材",
  Basketball: "篮球",
  "Dorm Desk Lamp": "宿舍台灯"
};

const seededProductDescriptions = {
  "Compact keyboard with blue switches, suitable for dorm study desk.": "紧凑型青轴键盘，适合宿舍书桌使用。",
  "Clean second-hand data structures textbook with notes in the first chapter only.": "二手数据结构教材，书页干净，仅第一章有少量笔记。",
  "Outdoor basketball used for one semester.": "室外篮球，使用过一个学期，弹性正常。",
  "Adjustable LED desk lamp with three brightness levels.": "可调节护眼台灯，支持三档亮度。"
};

const els = {
  apiDot: document.getElementById("apiDot"),
  apiStatus: document.getElementById("apiStatus"),
  loginForm: document.getElementById("loginForm"),
  logoutBtn: document.getElementById("logoutBtn"),
  userCard: document.getElementById("userCard"),
  productForm: document.getElementById("productForm"),
  marketList: document.getElementById("marketList"),
  mineList: document.getElementById("mineList"),
  orderList: document.getElementById("orderList"),
  searchKeyword: document.getElementById("searchKeyword"),
  searchCategory: document.getElementById("searchCategory"),
  searchBtn: document.getElementById("searchBtn"),
  refreshMarketBtn: document.getElementById("refreshMarketBtn"),
  refreshMineBtn: document.getElementById("refreshMineBtn"),
  refreshOrdersBtn: document.getElementById("refreshOrdersBtn"),
  toast: document.getElementById("toast")
};

async function api(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };
  if (state.token) {
    headers.Authorization = `Bearer ${state.token}`;
  }
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  });
  const text = await response.text();
  const payload = text ? JSON.parse(text) : null;
  if (!response.ok || (payload && payload.code !== 0)) {
    const message = payload && payload.message ? payload.message : `请求失败: ${response.status}`;
    throw new Error(message);
  }
  return payload ? payload.data : null;
}

function showToast(message, type = "normal") {
  els.toast.textContent = message;
  els.toast.className = `toast ${type}`;
  clearTimeout(showToast.timer);
  showToast.timer = setTimeout(() => {
    els.toast.classList.add("hidden");
  }, 2600);
}

function money(value) {
  return `￥${Number(value || 0).toFixed(2)}`;
}

function translate(value, dictionary) {
  return dictionary[value] || value || "";
}

function numericId(value) {
  return String(value || 0).padStart(6, "0");
}

function accountLabel(user) {
  if (!user) return "";
  return accountNames[user.username] || translate(user.nickname, displayNames) || "当前账号";
}

function displayProduct(product) {
  return {
    ...product,
    titleText: translate(product.title, seededProductNames),
    descriptionText: translate(product.description, seededProductDescriptions),
    categoryText: translate(product.category, categoryNames),
    conditionTextLabel: translate(product.conditionText, conditionNames),
    statusText: translate(product.status, statusNames),
    sellerNameText: translate(product.sellerName, displayNames),
    tradeLocationText: translate(product.tradeLocation, locationNames)
  };
}

function displayOrder(order) {
  return {
    ...order,
    productTitleText: translate(order.productTitle, seededProductNames),
    orderNoText: numericId(order.id),
    statusText: translate(order.status, statusNames),
    buyerNameText: translate(order.buyerName, displayNames),
    sellerNameText: translate(order.sellerName, displayNames),
    meetLocationText: translate(order.meetLocation, locationNames)
  };
}

function escapeHtml(value) {
  return String(value == null ? "" : value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function productCard(product, scope) {
  const view = displayProduct(product);
  const canBuy = state.user && product.sellerId !== state.user.id && product.status === "ON_SALE";
  const canManage = state.user && product.sellerId === state.user.id;
  return `
    <article class="card">
      <h3>${escapeHtml(view.titleText)}</h3>
      <div class="price">${money(product.price)}</div>
      <p class="description">${escapeHtml(view.descriptionText)}</p>
      <div class="meta">
        <span class="tag">${escapeHtml(view.categoryText)}</span>
        <span class="tag">${escapeHtml(view.conditionTextLabel || "未填写成色")}</span>
        <span class="tag">${escapeHtml(view.statusText)}</span>
      </div>
      <div class="meta">
        <span>卖家：${escapeHtml(view.sellerNameText || product.sellerId)}</span>
        <span>地点：${escapeHtml(view.tradeLocationText || "未填写")}</span>
      </div>
      <div class="card-actions">
        ${canBuy ? `<button data-action="buy" data-id="${product.id}" type="button">下单</button>` : ""}
        ${canManage && product.status === "DRAFT" ? `<button data-action="publish" data-id="${product.id}" type="button">发布</button>` : ""}
        ${canManage && (product.status === "ON_SALE" || product.status === "DRAFT") ? `<button class="warning" data-action="offShelf" data-id="${product.id}" type="button">下架</button>` : ""}
        ${scope === "market" ? `<button class="secondary" data-action="detail" data-id="${product.id}" type="button">详情</button>` : ""}
      </div>
    </article>
  `;
}

function orderCard(order) {
  const view = displayOrder(order);
  const isSeller = state.user && order.sellerId === state.user.id;
  const canConfirm = isSeller && order.status === "PENDING";
  const canComplete = state.user && (order.buyerId === state.user.id || order.sellerId === state.user.id) && order.status === "CONFIRMED";
  const canCancel = state.user && (order.buyerId === state.user.id || order.sellerId === state.user.id)
    && order.status !== "COMPLETED" && order.status !== "CANCELLED";
  return `
    <article class="order-card">
      <h3>${escapeHtml(view.productTitleText)}</h3>
      <div class="meta">
        <span class="tag">${escapeHtml(view.statusText)}</span>
        <span>订单编号：${escapeHtml(view.orderNoText)}</span>
        <span>金额：${money(order.price)}</span>
      </div>
      <div class="meta">
        <span>买家：${escapeHtml(view.buyerNameText)}</span>
        <span>卖家：${escapeHtml(view.sellerNameText)}</span>
        <span>地点：${escapeHtml(view.meetLocationText || "未填写")}</span>
      </div>
      <p class="description">${escapeHtml(order.remark || "无备注")}</p>
      <div class="order-actions">
        ${canConfirm ? `<button data-order-action="confirm" data-id="${order.id}" type="button">卖家确认</button>` : ""}
        ${canComplete ? `<button data-order-action="complete" data-id="${order.id}" type="button">完成交易</button>` : ""}
        ${canCancel ? `<button class="danger" data-order-action="cancel" data-id="${order.id}" type="button">取消订单</button>` : ""}
      </div>
    </article>
  `;
}

function renderUser() {
  if (!state.user) {
    els.userCard.classList.add("hidden");
    els.logoutBtn.classList.add("hidden");
    els.loginForm.classList.remove("hidden");
    return;
  }
  els.loginForm.classList.add("hidden");
  els.logoutBtn.classList.remove("hidden");
  els.userCard.classList.remove("hidden");
  els.userCard.innerHTML = `
    <strong>${escapeHtml(translate(state.user.nickname, displayNames))}</strong><br>
    账号：${escapeHtml(accountLabel(state.user))}<br>
    校区：${escapeHtml(translate(state.user.campus, campusNames) || "未填写")}<br>
    角色：${escapeHtml(translate(state.user.role, roleNames))}
  `;
}

function requireLogin() {
  if (!state.token) {
    showToast("请先登录");
    return false;
  }
  return true;
}

function pageContent(page) {
  if (!page) return [];
  return Array.isArray(page) ? page : (page.content || []);
}

function setEmpty(container, text) {
  container.innerHTML = `<div class="empty">${escapeHtml(text)}</div>`;
}

async function checkHealth() {
  try {
    await api("/health");
    els.apiDot.className = "dot ok";
    els.apiStatus.textContent = "后端已连接";
  } catch (error) {
    els.apiDot.className = "dot bad";
    els.apiStatus.textContent = "后端未连接";
  }
}

async function loadMarket() {
  els.marketList.innerHTML = `<div class="empty">加载商品中……</div>`;
  try {
    const keyword = els.searchKeyword.value.trim();
    const category = els.searchCategory.value;
    let data;
    if (keyword) {
      const params = new URLSearchParams({ keyword, page: "0", size: "30" });
      if (category) params.set("category", category);
      data = await api(`/search/products?${params.toString()}`);
    } else if (category) {
      const params = new URLSearchParams({ category, page: "0", size: "30" });
      data = await api(`/products?${params.toString()}`);
    } else {
      data = await api("/products?page=0&size=30");
    }
    const products = pageContent(data);
    els.marketList.innerHTML = products.length
      ? products.map(product => productCard(product, "market")).join("")
      : `<div class="empty">暂无商品</div>`;
  } catch (error) {
    setEmpty(els.marketList, error.message);
  }
}

async function loadMine() {
  if (!requireLogin()) {
    setEmpty(els.mineList, "登录后查看你发布的商品");
    return;
  }
  els.mineList.innerHTML = `<div class="empty">加载我的商品中……</div>`;
  try {
    const data = await api("/products/mine?page=0&size=30");
    const products = pageContent(data);
    els.mineList.innerHTML = products.length
      ? products.map(product => productCard(product, "mine")).join("")
      : `<div class="empty">你还没有发布商品</div>`;
  } catch (error) {
    setEmpty(els.mineList, error.message);
  }
}

async function loadOrders() {
  if (!requireLogin()) {
    setEmpty(els.orderList, "登录后查看你的订单");
    return;
  }
  els.orderList.innerHTML = `<div class="empty">加载订单中……</div>`;
  try {
    const data = await api("/orders?page=0&size=30");
    const orders = pageContent(data);
    els.orderList.innerHTML = orders.length
      ? orders.map(orderCard).join("")
      : `<div class="empty">暂无订单</div>`;
  } catch (error) {
    setEmpty(els.orderList, error.message);
  }
}

async function refreshActiveTab() {
  if (state.activeTab === "market") await loadMarket();
  if (state.activeTab === "mine") await loadMine();
  if (state.activeTab === "orders") await loadOrders();
}

async function login(event) {
  event.preventDefault();
  const username = document.getElementById("loginUsername").value.trim();
  const password = document.getElementById("loginPassword").value;
  try {
    const data = await api("/auth/login", {
      method: "POST",
      body: JSON.stringify({ username, password })
    });
    state.token = data.token;
    state.user = data.user;
    localStorage.setItem("campusTradeToken", state.token);
    localStorage.setItem("campusTradeUser", JSON.stringify(state.user));
    renderUser();
    showToast("登录成功");
    await refreshActiveTab();
  } catch (error) {
    showToast(error.message);
  }
}

async function logout() {
  state.token = "";
  state.user = null;
  localStorage.removeItem("campusTradeToken");
  localStorage.removeItem("campusTradeUser");
  renderUser();
  showToast("已退出登录");
  await refreshActiveTab();
}

async function createProduct(event) {
  event.preventDefault();
  if (!requireLogin()) return;
  const request = {
    title: document.getElementById("productTitle").value.trim(),
    category: document.getElementById("productCategory").value,
    price: Number(document.getElementById("productPrice").value),
    conditionText: document.getElementById("productCondition").value.trim(),
    tradeLocation: document.getElementById("productLocation").value.trim(),
    description: document.getElementById("productDescription").value.trim(),
    imageUrls: ""
  };
  try {
    const created = await api("/products", {
      method: "POST",
      body: JSON.stringify(request)
    });
    await api(`/products/${created.id}/publish`, { method: "POST" });
    event.target.reset();
    document.getElementById("productPrice").value = "20.00";
    document.getElementById("productCondition").value = "九成新";
    showToast("商品已创建并发布");
    await loadMarket();
    if (state.activeTab === "mine") await loadMine();
  } catch (error) {
    showToast(error.message);
  }
}

async function buyProduct(productId) {
  if (!requireLogin()) return;
  const meetLocation = prompt("请输入约定交易地点", "图书馆门口");
  if (meetLocation == null) return;
  try {
    await api("/orders", {
      method: "POST",
      body: JSON.stringify({
        productId,
        meetLocation,
        remark: "通过前端页面下单"
      })
    });
    showToast("下单成功");
    await loadMarket();
  } catch (error) {
    showToast(error.message);
  }
}

async function productAction(action, id) {
  try {
    if (action === "buy") {
      await buyProduct(id);
      return;
    }
    if (action === "detail") {
      const detail = await api(`/products/${id}`);
      const view = displayProduct(detail);
      alert(`${view.titleText}\n\n价格：${money(detail.price)}\n卖家：${view.sellerNameText}\n地点：${view.tradeLocationText || "未填写"}\n\n${view.descriptionText}`);
      return;
    }
    if (!requireLogin()) return;
    if (action === "publish") {
      await api(`/products/${id}/publish`, { method: "POST" });
      showToast("商品已发布");
    }
    if (action === "offShelf") {
      await api(`/products/${id}/off-shelf`, { method: "POST" });
      showToast("商品已下架");
    }
    await loadMarket();
    if (state.activeTab === "mine") await loadMine();
  } catch (error) {
    showToast(error.message);
  }
}

async function orderAction(action, id) {
  if (!requireLogin()) return;
  try {
    await api(`/orders/${id}/${action}`, { method: "POST" });
    showToast("订单状态已更新");
    await loadOrders();
    await loadMarket();
  } catch (error) {
    showToast(error.message);
  }
}

function switchTab(tab) {
  state.activeTab = tab;
  document.querySelectorAll(".tab").forEach(button => {
    button.classList.toggle("active", button.dataset.tab === tab);
  });
  document.querySelectorAll(".tab-page").forEach(page => {
    page.classList.toggle("active", page.id === `${tab}Tab`);
  });
  refreshActiveTab();
}

function bindEvents() {
  els.loginForm.addEventListener("submit", login);
  els.logoutBtn.addEventListener("click", logout);
  els.productForm.addEventListener("submit", createProduct);
  els.searchBtn.addEventListener("click", loadMarket);
  els.refreshMarketBtn.addEventListener("click", loadMarket);
  els.refreshMineBtn.addEventListener("click", loadMine);
  els.refreshOrdersBtn.addEventListener("click", loadOrders);

  document.querySelectorAll(".tab").forEach(button => {
    button.addEventListener("click", () => switchTab(button.dataset.tab));
  });

  document.body.addEventListener("click", event => {
    const productButton = event.target.closest("[data-action]");
    if (productButton) {
      productAction(productButton.dataset.action, Number(productButton.dataset.id));
    }
    const orderButton = event.target.closest("[data-order-action]");
    if (orderButton) {
      orderAction(orderButton.dataset.orderAction, Number(orderButton.dataset.id));
    }
  });

  els.searchKeyword.addEventListener("keydown", event => {
    if (event.key === "Enter") loadMarket();
  });
}

async function init() {
  bindEvents();
  renderUser();
  await checkHealth();
  await loadMarket();
}

init();

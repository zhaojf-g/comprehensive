const getters = {

    visitedViews: state => state.tagsView.visitedViews,
    cachedViews: state => state.tagsView.cachedViews,

    permission_routes: state => state.permission.routes,
    addRouters: state => state.permission.addRouters,

    systemTitle: state => state.settings.title,


    sidebar: state => state.app.sidebar,
    language: state => state.app.language,
    size: state => state.app.size,
    device: state => state.app.device,

    userName: state => state.user.name,
    token: state => state.user.token,
    roles: state => state.user.roles,
    menus: state => state.user.menus,
    userNodeId: state => state.user.userNodeId,
    userType: state => state.user.userType
}
export default getters

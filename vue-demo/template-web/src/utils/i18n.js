// translate router.meta.title, be used in breadcrumb sidebar tagsview
import i18n from '../lang'

export function generateTitle(title) {
  const hasKey = this.$te('route.' + title)

  if (hasKey) {
    // $t :this method from vue-i18n, inject in @/lang/index.js
    return this.$t('route.' + title)
  }
  return title
}

// translate router.meta.title, be used in breadcrumb sidebar tagsview
export function generateTitle2(title) {
  const hasKey = i18n.te('route.' + title)
  if (hasKey) {
    return i18n.t('route.' + title)
  }
  return title
}

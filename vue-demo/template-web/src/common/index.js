import i18n from '../lang'
export const orderStateList = [
  {
    label: i18n.t('order.orderStatePending'),//'待处理',
    value: 10
  }, {
    label: i18n.t('order.orderStateExecuting'),//'处理中',
    value: 20
  }, {
    label: i18n.t('order.orderStateCompleted'),//'已完成',
    value: 30
  }
]

export const orderState = {
  10: i18n.t('order.orderStatePending'),//'待处理',
  20: i18n.t('order.orderStateExecuting'),//'处理中',
  30: i18n.t('order.orderStateCompleted')//'已完成'
}

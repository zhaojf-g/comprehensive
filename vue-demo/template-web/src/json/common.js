export const productTypeList = [
  {
    label: '直连设备',
    value: 10
  },
  {
    label: '网关设备',
    value: 20
  },
  {
    label: '网关子设备',
    value: 30
  },
]
export const productTypeMap = {
  10: '直连设备',
  20: '网关设备',
  30: '网关子设备'
}

export const transportProtocolList = [
  {
    label: 'mqtt',
    value: 10
  },
  {
    label: 'tcp',
    value: 20
  }
]
export const transportProtocolMap = {
  10: 'mqtt',
}

export const componentTypeList = [
  {
    label: 'mqtt_client',
    value: 10
  },
  {
    label: 'mqtt_server',
    value: 11
  },
  {
    label: 'tcp_client',
    value: 20
  },
  {
    label: 'tcp_server',
    value: 21
  },
]

export const componentTypeMap = {
  10: 'mqtt_client',
  11: 'mqtt_server',
  20: 'tcp_client',
  21: 'tcp_server',
}

export const attributeType = [
  {
    label: '字符串',
    value: 'string'
  },
  {
    label: '整数',
    value: 'int'
  },
  {
    label: '长整数',
    value: 'long'
  },
  {
    label: '单精度浮点型',
    value: 'float'
  },
  {
    label: '双精度浮点型',
    value: 'double'
  },
  {
    label: '布尔',
    value: 'boolean'
  }
]

export const ruleType = [
  {
    label: '场景联动',
    value: 1
  },
  {
    label: '规则编排',
    value: 2
  },
  {
    label: '数据转发',
    value: 3
  }
]

export const ruleTypeMap = {
  1: '场景联动',
  2: '规则编排',
  3: '数据转发',
}
export const operators = [
  {
    label: '等于(=)',
    value: '='
  },
  {
    label: '不等于(!=)',
    value: '!='
  },
  {
    label: '大于(>)',
    value: '>'
  },
  {
    label: '小于(<)',
    value: '<'
  },
  {
    label: '大于等于(>=)',
    value: '>='
  },
  {
    label: '小于等于(<=)',
    value: '<='
  },
  {
    label: '模糊(%)',
    value: '%'
  },
]

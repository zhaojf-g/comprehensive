import i18n from '../lang'
// 必填且不超长校验
export function reqAndLenValidate(rule,value,callback,length) {
  if(!value || value===''){
    callback(new Error(i18n.t('common.contentRequired')));
  }else{
    let str = value.replace(/[^x00-xff]/g,'xxx');
    if(str.length > length){
      callback(new Error(i18n.t('common.contentTooLarge')));
    }else{
      callback();
    }
  }
}

//不超长校验
export function lenValidate(rule,value,callback,length) {
  if(!value || value===''){
    callback();
  }else{
    let str = value.replace(/[^x00-xff]/g,'xxx');
    if(str.length > length){
      callback(new Error(i18n.t('common.contentTooLarge')));
    }else{
      callback();
    }
  }
}

// 必填且必须是正整数校验
export function reqAndPositiveIntValidate(rule,value,callback) {
  if(!value || value===''){
    callback(new Error(i18n.t('common.contentRequired')));
  }else if(Number.isInteger(Number(value)) && Number(value) > 0){
    callback();
  }else{
    callback(new Error(i18n.t('common.contentReqPosInt')));
  }
}

//正整数校验
export function positiveIntValidate(rule,value,callback) {
  if(!value || value===''){
    callback();
  }else if(Number.isInteger(Number(value)) && Number(value) > 0){
    callback();
  }else{
    callback(new Error(i18n.t('common.contentReqPosInt')));
  }
}


export function formatDateToStr(times, pattern) {
  let d = new Date(times).Format('yyyy-MM-dd hh:mm:ss')
  if (pattern) {
    d = new Date(times).Format(pattern)
  }
  return d.toLocaleString()
}

export function formatStrToDate(date) {
  date = date.substring(0, 19)
  let d = date.replace(/-/g, '/')
  return new Date(d)
}

export function compareDateDifference(date1, date2) {
  let difference = (date1.getTime() - date2.getTime())
  return difference
}

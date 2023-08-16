import XLSX from 'xlsx'

async function readExcel(file) {
  const result = {
    code: 200,
    data: [],
    message: ''
  }

  const types = file.name.split('.')
  const type = types[types.length - 1]
  const fileType = [
    'xlsx', 'xls', 'XLSX', 'XLS'
  ].some(item => item === type)
  if (!fileType) {
    result.code = 400
    result.message = '格式错误！请重新选择'
    return result
  }

  const loaded = (row) => {
    return new Promise((resolve) => {
      const reader = new FileReader()
      reader.onload = function(e) {
        const data = e.target.result
        const wb = XLSX.read(data, {
          type: 'binary'
        })

        wb.SheetNames.forEach((sheetName) => {
          const json = XLSX.utils.sheet_to_json(wb.Sheets[sheetName], { header: 1, defval: '' })
          result.data.push(json)
        })
        resolve(true)
      }
      reader.readAsBinaryString(row)
    })
  }
  await loaded(file.raw)
  return result
}

export { readExcel }

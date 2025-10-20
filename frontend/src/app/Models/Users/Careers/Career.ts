export default interface Career{
    id : string
    name : string
    description : string
    durationMonths : number
    monthlyFee : number
    annualFee : number
    active : boolean
    careerType?: string
}
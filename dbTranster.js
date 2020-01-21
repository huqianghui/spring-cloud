var dataSourceItems = db.user_profile.find();
var insertCount=0;
var existingCount=0;
dataSourceItems.forEach(element => {
    if(db.user_profile2.find({"loginAccounts": {$elemMatch:{"login_account_type_code": element.loginAccounts[0].login_account_type_code,"login_account_identity": element.loginAccounts[0].login_account_identity}}})){
        existingCount++;
    }else{
        insertCount++;
        db.itemTarget.insert(elemenet);  
        //TODO Mapping
        
    }
    
});

print("create records: " + insertCount );
print("existing records: " + existingCount );
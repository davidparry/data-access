# Getting Started

## Scenario 1 - Local Current Project Security Vulnerability
### Steps:
1. Select [Full project](assets/context.png) to add to [Extra Context](assets/entire-file.png)  
2. [Focus On - Current File](assets/current-file.png) make sure report.txt is open in your editor.
3. Paste this [ask](assets/ask.png) into Ask window
```text 
review the security scan report and inspect the project for the other classes that may also have the vulnerability. Identify those classes and apply the suggests fix for those classes. 
```
4. submit the ask click (Enter) [Report](assets/response.md)
#### Points:
- The local project the vulnerabilities where found!
- Not all the files where selected since they were fixed. (Filtered out SQL that complied already)
  - OrderDao, StockDao are fixed already for this specific Vulnerability.

## Scenario 2 - Remote Enterprise Security Vulnerability 
### Steps:
1. Bring up the [Registry window](assets/tools.png) in your IDE:
   - Select Tools -> Internal Actions -> Registry
2. Set the key `ai.codium.use_custom_endpoint` = [https://proxy.remote-rag.dev.codium-inc.com/](assets/restart-now.png)
3. Select [Company Codebases](assets/company-code.png) add [Select all codebases](assets/select-all.png) to [Extra Context](assets/all-company.png)
2. [Focus On - Current File](assets/current-file.png) make sure report.txt is open in your editor.
3. Paste this [ask](assets/remote-ask.png) into Ask window
```text 
review the security scan report and inspect the project for the other classes that may also have the vulnerability. Identify those classes and apply the suggests fix for those classes. 
```
4. submit the ask click (Enter) [Report](assets/response-enterprise.md)
#### Points:
- The Remote projects vulnerabilities where found!


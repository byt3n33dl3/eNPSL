# Logs collector (Still in testing)
# For More script goto -> https://github.com/Whitecat18/Powershell-Scripts-for-Hackers-and-Pentesters


$tempDir = [System.IO.Path]::Combine($env:TEMP, "LogScripts")
$customDir = "C:\Users\Abishek\systemlogs"


$null = New-Item -ItemType Directory -Force -Path $tempDir
$null = New-Item -ItemType Directory -Force -Path $customDir

function Save-Logs {
    param(
        [string]$tempLogFilePath,
        [string]$customLogFilePath,
        [string]$logType,
        [object]$logs
    )

    Write-Host "$logType logs have been saved to (temp): $tempLogFilePath"
    $logs | Format-Table -AutoSize | Out-File -FilePath $tempLogFilePath

    Write-Host "$logType logs have been saved to (custom): $customLogFilePath"
    $logs | Format-Table -AutoSize | Out-File -FilePath $customLogFilePath
}

function Get-MyComputerInfo {
    $computerInfo = Get-ComputerInfo | Out-String
    return $computerInfo
}

function Get-Authenticode {
    $authenticode = Get-ChildItem $PSHOME\*.* | ForEach-Object { Get-AuthenticodeSignature $_ } | Where-Object { $_.Status -eq "Valid" }
    return $authenticode
}


function Get-RunningProcesses {
    $runningProcesses = Get-Process | Select-Object Name, Id, CPU, Memory
    return $runningProcesses
}

function Get-WindowsUpdates {
    $windowsUpdates = Get-HotFix | Select-Object Description, HotFixID, InstalledOn
    return $windowsUpdates
}

function Get-MyNetAdapter {
    $networkAdapterInfo = Get-NetAdapter -Name "*" -IncludeHidden | Format-List -Property "Name", "InterfaceDescription", "InterfaceName", "MacAddress"
    return $networkAdapterInfo
}

function Get-SystemInformation {
    $systemInfo = @()  # Initialize an empty array

    $systemInfo += Get-CimInstance -ClassName Win32_ComputerSystem | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_BIOS | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_Processor | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_LogicalDisk | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_PhysicalMemory | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_Product | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_PnPEntity | Select-Object *
    $systemInfo += Get-CimInstance -ClassName Win32_DiskDrive | Select-Object *

    return $systemInfo
}

function Get-FirewallRules {
    $firewallRules = Get-NetFirewallRule | Select-Object DisplayName, Action, Direction, Enabled
    return $firewallRules
}

function Get-EnvironmentVariables {
    $envVariables = Get-ChildItem Env: | Select-Object Name, Value
    return $envVariables
}

function Get-ScheduledTasks {
    $scheduledTasks = Get-ScheduledTask | Select-Object TaskName, State, NextRunTime
    return $scheduledTasks
}

function Get-LastRebootTime {
    $lastReboot = Get-CimInstance Win32_OperatingSystem | Select-Object LastBootUpTime
    return $lastReboot
}

$functions = @(
    'Get-RunningProcesses','Get-MyComputerInfo', 'Get-WindowsUpdates', 'Get-MyNetAdapter',
    'Get-SystemInformation', 'Get-FirewallRules', 'Get-EnvironmentVariables',
    'Get-ScheduledTasks', 'Get-LastRebootTime','Get-Authenticode'
)

foreach ($functionName in $functions) {
    $functionResult = Invoke-Expression "$functionName"
    $tempLogPath = [System.IO.Path]::Combine($tempDir, "$functionName-LogFile.txt")
    $customLogPath = [System.IO.Path]::Combine($customDir, "$functionName-LogFile.txt")
    Save-Logs -tempLogFilePath $tempLogPath -customLogFilePath $customLogPath -logType $functionName -logs $functionResult
}

Write-Host "Info Collected"

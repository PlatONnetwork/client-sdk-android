-   [概览](#概览)

-   [快速入门](#快速入门)
    -   [安装或引入](#安装或引入)
    -   [初始化代码](#初始化代码)
<a name="top"></a>
-   [合约](#合约)
    -   [质押接口](#质押接口)
	    -  [发起质押](#staking)
	    -  [修改质押信息](#updateStakingInfo)
	    -  [增持质押](#addStaking)
	    -  [撤销质押](#unStaking)
	    -  [查询节点质押信息](#getStakingInfo)
		-  [查询当前结算周期的区块奖励](#getPackageReward)
		-  [查询当前结算周期的质押奖励](#getStakingReward)
		-  [查询打包区块的平均时间](#getAvgPackTime)
    -   [委托接口](#委托接口)
	    -   [发起委托](#delegate)
	    -   [撤销委托](#unDelegate)
	    -   [查询节点委托信息](#getDelegateInfo)
	    -   [查询当前账户地址所委托的节点的NodeID和质押Id](#getRelatedListByDelAddr)
    -   [节点列表接口](#节点列表接口)
	    -   [查询当前结算周期的验证人列表](#getVerifierList)
	    -   [查询当前共识周期的验证人列表](#getValidatorList)
	    -   [查询所有实时的候选人列表](#getCandidateList)
    -   [治理接口](#治理接口)
	    -   [提交文本提案](#submitProposal)
	    -   [提交升级提案](#submitProposal)
	    -   [提交参数提案](#submitProposal)
	    -   [给提案投票](#vote)
	    -   [版本声明](#declareVersion)
	    -   [查询提案](#getProposal)
	    -   [查询提案结果](#getTallyResult)
	    -   [查询提案列表](#getProposalList)
	    -   [查询生效版本](#getActiveVersion)
    -   [举报惩罚接口](#举报惩罚接口)
	    -   [举报多签](#reportDoubleSign)
	    -   [查询节点是否有多签过](#checkDoubleSign)
    -   [锁仓计划接口](#锁仓计划接口)
	    -   [创建锁仓信息](#createRestrictingPlan)
	    -   [获取锁仓信息](#getRestrictingInfo)
-   [web3](#web3)
    -   [web3 eth相关 (标准JSON RPC )](#web3-eth相关-标准json-rpc)
        -   [获取当前客户端版本](#web3ClientVersion)
        -   [给定数据的keccak-256（不是标准sha3-256）](#web3Sha3)
        -   [获取当前网络ID](#netVersion)
        -   [获取客户端是否正在积极侦听网络连接](#netListening)
        -   [获取当前连接到客户端的对等体的数量](#netPeerCount)
        -   [获取当前platon协议版本](#platonProtocolVersion)
        -   [获取当前platon同步状态](#platonSyncing)
        -   [获取gas当前价格](#platonGasPrice)
        -   [获取客户端拥有的地址列表](#platonAccounts)
        -   [获取当前最高块高](#platonBlockNumber)
        -   [获取地址余额](#platonGetBalance)
        -   [获取给定地址的存储位置返回值](#platonGetStorageAt)
        -   [获取根据区块hash查询区块中交易个数](#platonGetBlockTransactionCountByHash)
        -   [获取根据地址查询该地址发送的交易个数](#platonGetTransactionCount)
        -   [获取区块块高查询块高中的交易总数](#platonGetBlockTransactionCountByNumber)
        -   [获取给定地址的代码](#platonGetCode)
        -   [给数据签名](#platonSign)
        -   [发送服务代签名交易](#platonSendTransaction)
        -   [发送交易](#platonSendRawTransaction)
        -   [获取当前网络ID](#platonCall)
        -   [估算合约方法gas用量](#platonEstimateGas)
        -   [根据区块hash查询区块信息](#platonGetBlockByHash)
        -   [根据区块高度查询区块信息](#platonGetBlockByNumber)
        -   [根据区块hash查询区块中指定序号的交易](#根据区块高度查询区块信息)
        -   [根据区块高度查询区块中指定序号的交易](#platonGetTransactionByBlockNumberAndIndex)
        -   [根据交易hash查询交易回执](#platonGetTransactionReceipt)
        -   [创建过滤器](#platonNewFilter)
        -   [创建区块过滤器](#platonNewBlockFilter)
        -   [创建确认中交易过滤器](#platonNewPendingTransactionFilter)
        -   [写在具有指定编号的过滤器。当不在需要监听时，总是需要执行该调用](#platonUninstallFilter)
        -   [轮询指定的过滤器，并返回自上次轮询之后新生成的日志数组 ](#platonGetFilterChanges)
        -   [轮询指定的过滤器，并返回自上次轮询之后新生成的日志数组](#platonGetFilterLogs)
        -   [返回指定过滤器中的所有日志](#platonGetLogs)
        -   [查询待处理交易](#platonPendingTransactions)
        -   [在本地数据库中存入字符串](#dbPutString)
        -   [从本地数据库读取字符串](#dbGetString)
        -   [将二进制数据写入本地数据库](#dbPutHex)
        -   [从本地数据库中读取二进制数据](#dbGetHex)
        -   [返回双签举报数据](#platonEvidences)
        -   [获取代码版本](#getProgramVersion)
        -   [获取bls的证明](#getSchnorrNIZKProve)
        -   [获取PlatON参数配置](#getEconomicConfig)


# 概览
> Android SDK是PlatON面向Android开发者，提供的PlatON公链的android开发工具包


# 快速入门

## 安装或引入

### 环境要求
1. jdk1.8

### maven
1. 配置仓库

    1. maven项目配置
    ```
    <repository>
	    <id>platon-public</id>
	    <url>https://sdk.platon.network/nexus/content/groups/public/</url>
	</repository>
    ```

    2. gradle项目配置
    ```
    repositories {
        maven { url "https://sdk.platon.network/nexus/content/groups/public/" }
    }
    ```

2. maven方式引用

    Java 8:
    ```
    <dependency>
        <groupId>com.platon.client</groupId>
        <artifactId>core</artifactId>
        <version>0.6.0</version>
    </dependency>
    ```
    Android:

    ```
    <dependency>
        <groupId>com.platon.client</groupId>
        <artifactId>core</artifactId>
        <version>0.6.0-android</version>
    </dependency>
    ```

3. gradle方式引用

    Java 8:
    ```
    compile "com.platon.client:core:0.6.0"
    ```
    Android:

    ```
    compile "com.platon.client:core:0.6.0-android"
    ```


## 初始化代码

Java 8

```
Web3j web3 = Web3j.build(new HttpService("http://localhost:6789"));
```

Android

```
Web3j web3 = Web3jFactory.build(new HttpService("http://localhost:6789"));
```


# 合约

<a name="catalog"></a>

所有接口统一返回BaseResponce，定义如下:

| **属性名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| code | int |  响应错误码，0代表成功  |
| data | T |  响应的数据，为泛型，不同的接口返回的类型不一样  |
| errMsg | String |  异常提示  |
| transactionReceipt | TransactionReceipt |  交易回执  |


## 质押接口

<a name="staking"></a>

[回到接口目录](#top)

### 初始化

获取StakingContract实例：

```
//查询操作
public static StakingContract load(Web3j web3j);
//修改操作，钱包和链id必传
public static StakingContract load(Web3j web3j, Credentials credentials, long chainId);

```

### staking

>发起质押

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| stakingParam | StakingParam |  质押参数包装类,[StakingParam](#StakingParam)  |


<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="updateStakingInfo"></a>

[回到接口目录](#top)

### updateStakingInfo
>修改质押信息

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| updateStakingParam |UpdateStakingParam | 修改质押信息包装类,[UpdateStakingParam](#UpdateStakingParam)  |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="addStaking"></a>

[回到接口目录](#top)

### addStaking
>增持质押

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| nodeId | String |  候选人节点Id,16进制格式,0x开头  |
| stakingAmountType | StakingAmountType |  枚举，账户自由金额和账户的锁仓金额做质押，[StakingAmountType](#StakingAmountType) |
| amount | BigInteger |  增持的金额，单位为von  |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="unStaking"></a>

[回到接口目录](#top)

### unStaking
>撤销质押

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| nodeId | String |  候选人节点Id,16进制格式,0x开头  |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="getStakingInfo"></a>

[回到接口目录](#top)

### getStakingInfo
>查询节点质押信息

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| nodeId | String |  候选人节点Id,16进制格式,0x开头  |

<br/>**返回值** : [统一格式返回](#catalog),data为[Node](#Node)</br>

[回到接口目录](#top)

### getPackageReward
>查询当前结算周期的区块奖励

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为当前结算周期的区块奖励，数据型为BigInter</br>

[回到接口目录](#top)

### getStakingReward
>查询当前结算周期的质押奖励

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为当前结算周期的质押奖励，数据型为BigInter</br>

[回到接口目录](#top)

### getAvgPackTime
>查询打包区块的平均时间

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为打包区块的平均时间，数据型为BigInter</br>

[回到接口目录](#top)

## 委托接口

### 初始化

获取DelegateContract实例

```
//查询操作
public static DelegateContract load(Web3j web3j);
//修改操作，钱包和链id必传
public static DelegateContract load(Web3j web3j, Credentials credentials, long chainId);

```
<a name="delegate"></a>

[回到接口目录](#top)

### delegate
>发起委托

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| nodeId | String |  候选人节点Id,16进制格式,0x开头  |
| stakingAmountType | StakingAmountType |  枚举，账户自由金额和账户的锁仓金额做质押，[StakingAmountType](#StakingAmountType) |
| amount | BigInteger |  增持的金额，单位为von  |

<br>**返回值** : [统一格式返回](#catalog)<br/>

<a name="unDelegate"></a>

[回到接口目录](#top)

### unDelegate
>撤销委托

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| nodeId | String |  候选人节点Id,16进制格式,0x开头  |
| stakingAmountType | StakingAmountType |  枚举，账户自由金额和账户的锁仓金额做质押，[StakingAmountType](#StakingAmountType) |
| amount | BigInteger |  增持的金额，单位为von  |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="getDelegateInfo"></a>

[回到接口目录](#top)

### getDelegateInfo
>查询节点委托信息

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| nodeId | String |  候选人节点Id,16进制格式,0x开头  |
| delAddr | String |  委托人账户地址,16进制格式,0x开头 |
| stakingBlockNum | BigInteger |  质押的块高  |

<br/>**返回值** : [统一格式返回](#catalog),data为[Delegation](#Delegation)</br>

<a name="getRelatedListByDelAddr"></a>

[回到接口目录](#top)

### getRelatedListByDelAddr
>查询当前账户地址所委托的节点的NodeID和质押Id


**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| address | String |  委托人的账户地址  |

<br/>**返回值** : [统一格式返回](#catalog),data为[DelegationIdInfo](#DelegationIdInfo)</br>

## 节点列表接口

### 初始化

获取NodeContract实例

```
//查询操作
public static NodeContract load(Web3j web3j);
```

<a name="getVerifierList"></a>

[回到接口目录](#top)

### getVerifierList
>查询当前结算周期的验证人队列

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为[Node](#Node)列表</br>

<a name="getValidatorList"></a>

[回到接口目录](#top)

### getValidatorList
>查询当前共识周期的验证人列表

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为[Node](#Node)列表</br>

<a name="getCandidateList"></a>

[回到接口目录](#top)

### getCandidateList
>查询所有实时的候选人列表

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为[Node](#Node)列表</br>

## 治理接口

### 初始化

获取ProposalContract实例

```
//查询操作
public static ProposalContract load(Web3j web3j);
//修改操作
public static ProposalContract load(Web3j web3j, Credentials credentials, long chainId)
```
<a name="submitProposal"></a>

[回到接口目录](#top)

### submitProposal
>提交(文本、升级、参数)提案


<br/>文本提案，通过Proposal.createSubmitTextProposalParam()构建</br>

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| verifier | String |  用于接受出块奖励和质押奖励的收益账户  |
| piPid | String |  外部Id(有长度限制，给第三方拉取节点描述的Id)  |


<br/>版本提案，通过Proposal.createSubmitVersionProposalParam()构建</br>

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| verifier | String |  用于接受出块奖励和质押奖励的收益账户  |
| piPid | String |  外部Id(有长度限制，给第三方拉取节点描述的Id)  |
| newVersion | BigInteger |  升级版本  |
| endVotingBlock | BigInteger |  提案投票结束的块高  |

<br/>取消提案，通过Proposal.createSubmitCancelProposalParam()构建</br>

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| verifier | String |  用于接受出块奖励和质押奖励的收益账户  |
| piPid | String |  外部Id(有长度限制，给第三方拉取节点描述的Id)  |
| endVotingBlock | BigInteger |  提案投票结束的块高  |
| toBeCanceled | String |  提案要取消的升级提案ID  |

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="vote"></a>

[回到接口目录](#top)

### vote
>给提案投票

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
|programVersion|ProgramVersion|程序的真实版本，治理rpc接口admin_getProgramVersion获取|
| voteOption | VoteOption |  枚举，投票选项，取值为YEAS(支持)、NAYS(反对)、ABSTENTIONS(弃权)  |
| proposalID | String |  提案ID  |
| verifier | String |  投票验证人 |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="declareVersion"></a>

[回到接口目录](#top)

### declareVersion
>版本声明

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
|programVersion|ProgramVersion|程序的真实版本，治理rpc接口admin_getProgramVersion获取|
| verifier | String |  声明的验证人 |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="getProposal"></a>

[回到接口目录](#top)

### getProposal
>查询提案

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| proposalId | String |  提案id |

<br/>**返回值** : [统一格式返回](#catalog),data为[Proposal](#Proposal)</br>

<a name="getTallyResult"></a>

[回到接口目录](#top)

### getTallyResult
>查询提案结果

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| proposalId | String |  提案id |

<br/>**返回值** : [统一格式返回](#catalog),data为[TallyResult](#TallyResult)</br>

<a name="getProposalList"></a>

[回到接口目录](#top)

### getProposalList
>查询提案列表

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为[TallyResult](#TallyResult)列表</br>

<a name="getActiveVersion"></a>

[回到接口目录](#top)

### getActiveVersion
>查询已生效的版本

**入参**

无

<br/>**返回值** : [统一格式返回](#catalog),data为String，生效的版本号</br>


## 举报惩罚接口

### 初始化

获取SlashContract实例

```
//查询操作
public static SlashContract load(Web3j web3j);
//修改操作
public static SlashContract load(Web3j web3j, Credentials credentials, long chainId)
```
<a name="reportDoubleSign"></a>

[回到接口目录](#top)

### reportDoubleSign
>举报举报多签

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| data | String |  证据的json值 |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="checkDoubleSign"></a>

[回到接口目录](#top)

### checkDoubleSign
>查询节点是否已被举报过多签

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| doubleSignType | DuplicateSignType |  枚举,双签类型,[DuplicateSignType](#DuplicateSignType) |
| address | String |  举报的节点地址，16进制，以0x开头 |
| blockNumber | BigInteger |  举报多签的块高  |

<br/>**返回值** : [统一格式返回](#catalog)，data为举报的交易Hash</br>

## 锁仓计划接口

### 初始化

获取RestrictingPlanContract实例

```
//查询操作
public static RestrictingPlanContract load(Web3j web3j);
//修改操作
public static RestrictingPlanContract load(Web3j web3j, Credentials credentials, long chainId)
```
<a name="createRestrictingPlan"></a>

[回到接口目录](#top)

### createRestrictingPlan
>创建锁仓计划

**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| account | String |  锁仓释放到账账户，16进制，以0x开头  |
| restrictingPlanList | List<RestrictingPlan> |  锁仓计划列表，[RestrictingPlan](#RestrictingPlan) |

<br/>**返回值** : [统一格式返回](#catalog)</br>

<a name="getRestrictingInfo"></a>

[回到接口目录](#top)

### getRestrictingInfo
>获取锁仓信息
**入参**

| **参数名** | **类型** | **参数说明** |
| ------ | ------ | ------ |
| account | String |  锁仓释放到账账户，16进制，以0x开头  |

<br/>**返回值** : [统一格式返回](#catalog)，data为[RestrictingItem](#RestrictingItem)</br>


# 实体类定义

<a name="StakingParam"></a>
## StakingParam

| *参数名* | *类型* | *参数说明* |
| ------ | ------ | ------ |
| nodeId | String |  被质押的节点id，16进制，以0x开头  |
| amount | BigInteger |  质押的金额，单位为von  |
| stakingAmountType | StakingAmountType |  枚举，账户自由金额和账户的锁仓金额做质押，[StakingAmountType](#StakingAmountType)  |
| benifitAddress | String |  用于接受出块奖励和质押奖励的收益账户  |
| externalId | String |  外部Id(有长度限制，给第三方拉取节点描述的Id)  |
| nodeName | String |  被质押节点的名称(有长度限制，表示该节点的名称)  |
| webSite | String |  节点的第三方主页(有长度限制，表示该节点的主页)  |
| details | String |  节点的描述(有长度限制，表示该节点的描述)  |
| blsPubKey | String |  bls的公钥  |
| programVersion|ProgramVersion|程序的真实版本，治理rpc接口admin_getProgramVersion获取|
| blsProof|String| bls的证据，通过rpc接口admin_getSchnorrNIZKProve获取|

<a name="UpdateStakingParam"></a>
## UpdateStakingParam

| *参数名* | *类型* | *参数说明* |
| ------ | ------ | ------ |
| nodeId | String |  被质押的节点id，16进制，以0x开头  |
| benifitAddress | String |  用于接受出块奖励和质押奖励的收益账户  |
| externalId | String |  外部Id(有长度限制，给第三方拉取节点描述的Id)  |
| nodeName | String |  被质押节点的名称(有长度限制，表示该节点的名称)  |
| webSite | String |  节点的第三方主页(有长度限制，表示该节点的主页)  |
| details | String |  节点的描述(有长度限制，表示该节点的描述)  |

<a name="Node"></a>
## Node

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| nodeId | String |  被质押的节点Id(也叫候选人的节点Id)  |
| stakingAddress | String |  发起质押时使用的账户(后续操作质押信息只能用这个账户，撤销质押时，von会被退回该账户或者该账户的锁仓信息中)  |
| benifitAddress | String |  用于接受出块奖励和质押奖励的收益账户  |
| stakingTxIndex | BigInteger |  发起质押时的交易索引  |
| programVersion | BigInteger |  被质押节点的PlatON进程的真实版本号(获取版本号的接口由治理提供)  |
| status | BigInteger |  候选人的状态  |
| stakingEpoch | BigInteger |  当前变更质押金额时的结算周期  |
| stakingBlockNum | BigInteger |  发起质押时的区块高度  |
| shares | BigInteger |  当前候选人总共质押加被委托的von数目  |
| released | BigInteger |  发起质押账户的自由金额的锁定期质押的von  |
| releasedHes | BigInteger |  发起质押账户的自由金额的犹豫期质押的von  |
| restrictingPlan | BigInteger |  发起质押账户的锁仓金额的锁定期质押的von  |
| restrictingPlanHes | BigInteger |  发起质押账户的锁仓金额的犹豫期质押的von  |
| externalId | String |  外部Id(有长度限制，给第三方拉取节点描述的Id)  |
| nodeName | String |  被质押节点的名称(有长度限制，表示该节点的名称) |
| website | String |  节点的第三方主页(有长度限制，表示该节点的主页)  |
| details | String |  节点的描述(有长度限制，表示该节点的描述) |
| validatorTerm | BigInteger |  验证人的任期(在结算周期的101个验证人快照中永远是0，只有在共识轮的验证人时才会被有值，刚被选出来时也是0，继续留任时则+1)  |

<a name="Delegation"></a>
## Delegation

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| delegateAddress | String |  委托人的账户地址  |
| nodeId | String |  验证人的节点Id |
| stakingBlockNum | BigInteger |  发起质押时的区块高度  |
| delegateEpoch | BigInteger |  最近一次对该候选人发起的委托时的结算周期  |
| delegateReleased | BigInteger |  发起委托账户的自由金额的锁定期委托的von(获取版本号的接口由治理提供)  |
| delegateReleasedHes | BigInteger |  发起委托账户的自由金额的犹豫期委托的von  |
| delegateLocked | BigInteger |  发起委托账户的锁仓金额的锁定期委托的von  |
| delegateLockedHes | BigInteger |  发起委托账户的锁仓金额的犹豫期委托的von  |
| delegateReduction | BigInteger |  处于撤销计划中的von  |

<a name="DelegationIdInfo"></a>
## DelegationIdInfo

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| address | String |  验证人节点的地址  |
| nodeId | String |  验证人的节点Id |

<a name="Proposal"></a>
## Proposal

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| proposalId | String |  提案id  |
| proposer | String |  提案节点ID |
| proposalType | int |  提案类型， [ProposalType](ProposalType)  |
| piPid | String |  提案PIPID  |
| submitBlock | BigInteger |  提交提案的块高(获取版本号的接口由治理提供)  |
| endVotingBlock | BigInteger |  提案投票结束的块高  |
| newVersion | BigInteger |  升级版本号  |
| toBeCanceled | String |  提案要取消的升级提案ID  |
| activeBlock | BigInteger |  生效块高  |
| verifier | String |  提交提案的验证人  |

<a name="RestrictingItem"></a>
## RestrictingItem

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| balance | BigInteger |  提交提案的块高(获取版本号的接口由治理提供)  |
| pledge | BigInteger |  提案投票结束的块高  |
| debt | BigInteger |  升级版本号  |
| info | List<[RestrictingInfo](#RestrictingInfo)> |  生效块高  |

## RestrictingInfo

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| blockNumber | BigInteger |  释放区块高度  |
| amount | BigInteger |  目标区块上待释放的金额  |

<a name="RestrictingPlan"></a>
## RestrictingPlan

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| epoch | BigInteger |  表示结算周期的倍数  |
| amount | BigInteger |  目标区块上待释放的金额  |

<a name="TallyResult"></a>
## TallyResult

| **参数名** | **类型** | **属性说明** |
| ------ | ------ | ------ |
| proposalID | String |  提案ID  |
| yeas | BigInteger |  赞成票  |
| nays | BigInteger |  反对票  |
| abstentions | BigInteger |  弃权票  |
| accuVerifiers | BigInteger |  在整个投票期内有投票资格的验证人总数  |
| status | int |  状态  |

<a name="StakingAmountType"></a>
## StakingAmountType

| **名称** | **值** | **描述** |
| ------ | ------ | ------ |
| FREE_AMOUNT_TYPE | 0 |自由金额  |
| RESTRICTING_AMOUNT_TYPE |  1 |锁仓金额  |

<a name="ProposalType"></a>
## ProposalType

| **名称** | **值** | **描述** |
| ------ | ------ | ------ |
| TEXT_PROPOSAL |  0x01  | 文本提案 |
| VERSION_PROPOSAL |  0x02  | 版本提案 |
| PARAM_PROPOSAL |  0x03  | 参数提案 |
| CANCEL_PROPOSAL |  0x04  | 取消提案 |

<a name="DuplicateSignType"></a>
## DuplicateSignType

| **名称** | **值** | **描述** |
| ------ | ------ | ------ |
| PREPARE_BLOCK |  1  | prepareBlock |
| PREPARE_VOTE |  2  |  prepareVote |
| VIEW_CHANGE |  3  | viewChange |

<a name="VoteOption"></a>
## VoteOption

| **名称** | **值** | **描述** |
| ------ | ------ | ------ |
| YEAS |  1  | 支持 |
| NAYS |  2  |  反对 |
| ABSTENTIONS |  3  | 弃权 |

#
## web3 platon相关 (标准JSON RPC )
<a name="web3ClientVersion"></a>
### 1.web3ClientVersion

> 返回当前客户端版本

* **参数**

  无

* **返回值**

```android
Request<?, Web3ClientVersion>
```

Web3ClientVersion属性中的string即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3j.Web3jFactory(new HttpService("http://127.0.0.1:6789"));
Request <?, Web3ClientVersion> request = currentValidWeb3j.web3ClientVersion();
String version = request.send().getWeb3ClientVersion();
```

<a name="web3Sha3"></a>
### 2.web3Sha3

> 返回给定数据的keccak-256（不是标准sha3-256）

* **参数**

  String ：加密前的数据

* **返回值**

```android
Request<?, Web3Sha3>
```

Web3Sha3属性中的string即为对应存储数据

* **示例**

```java
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String date = "";
Request <?, Web3Sha3> request = currentValidWeb3j.web3Sha3(date);
String resDate = request.send().getWeb3ClientVersion();
```

<a name="netVersion"></a>
### 3.netVersion

> 返回当前网络ID

* **参数**

  无

* **返回值**

```android
Request<?, NetVersion>
```

NetVersion属性中的string即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, NetVersion> request = currentValidWeb3j.netVersion();
String version = request.send().getNetVersion();
```

<a name="netListening"></a>
### 4.netListening

> 如果客户端正在积极侦听网络连接，则返回true

* **参数**

  无

* **返回值**

```android
Request<?, NetListening>
```

NetListening属性中的boolean即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, NetListening> request = currentValidWeb3j.netListening();
boolean req = request.send().isListening();
```

<a name="netPeerCount"></a>
### 5.netPeerCount

> 返回当前连接到客户端的对等体的数量

* **参数**

  ​	无

* **返回值**

```android
Request<?, NetPeerCount>
```

NetPeerCount属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, NetPeerCount> request = currentValidWeb3j.netPeerCount();
BigInteger req = request.send().getQuantity();
```

<a name="platonProtocolVersion"></a>
### 6.platonProtocolVersion

> 返回当前platon协议版本

* **参数**

  无

* **返回值**

```android
Request<?, PlatonProtocolVersion>
```

PlatonProtocolVersion属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonProtocolVersion> request = currentValidWeb3j.platonProtocolVersion();
String req = request.send().getProtocolVersion();
```

<a name="platonSyncing"></a>
### 7.platonSyncing

> 返回一个对象，其中包含有关同步状态的数据或false

* **参数**

  无

* **返回值**

```android
Request<?, PlatonSyncing>
```

PlatonSyncing属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonSyncing> request = currentValidWeb3j.platonSyncing();
boolean req = request.send().isSyncing();
```

<a name="platonGasPrice"></a>
### 8.platonGasPrice

> 返回gas当前价格

* **参数**

  无

* **返回值**

```android
Request<?, PlatonGasPrice>
```

PlatonGasPrice属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonGasPrice> request = currentValidWeb3j.platonGasPrice();
BigInteger req = request.send().getGasPrice();
```

<a name="platonAccounts"></a>
### 9.platonAccounts

> 返回客户端拥有的地址列表

* **参数**

  无

* **返回值**

```android
Request<?, PlatonAccounts>
```

PlatonAccounts属性中的String数组即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonAccounts> request = currentValidWeb3j.platonAccounts();
List<String> req = request.send().getAccounts();
```

<a name="platonBlockNumber"></a>
### 10.platonBlockNumber

> 返回当前最高块高

* **参数**

  无

* **返回值**

```android
Request<?, PlatonBlockNumber>
```

PlatonBlockNumber属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonBlockNumber> request = currentValidWeb3j.platonBlockNumber();
BigInteger req = request.send().getBlockNumber();
```

<a name="platonGetBalance"></a>
### 11.platonGetBalance

> 返回查询地址余额

* **参数**

    - String ： address 需要查询的地址
    - DefaultBlockParameter:

      - DefaultBlockParameterName.LATEST  最新块高(默认)

      - DefaultBlockParameterName.EARLIEST 最低块高

      - DefaultBlockParameterName.PENDING 未打包交易

      - DefaultBlockParameter.valueOf(BigInteger  blockNumber) 指定块高



* **返回值**

```android
Request<?, PlatonGetBalance>
```

PlatonGetBalance属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String address = "";
Request <?, PlatonGetBalance> request = currentValidWeb3j.platonGetBalance(address,DefaultBlockParameterName.LATEST);
BigInteger req = request.send().getBlockNumber();
```

<a name="platonGetStorageAt"></a>
### 12.platonGetStorageAt

> 从给定地址的存储位置返回值

* **参数**
    - String : address  存储地址
    - BigInteger: position 存储器中位置的整数
    - DefaultBlockParameter:
      -  DefaultBlockParameterName.LATEST  最新块高(默认)
      -  DefaultBlockParameterName.EARLIEST 最低块高
      -  DefaultBlockParameterName.PENDING 未打包交易
      -  DefaultBlockParameter.valueOf(BigInteger blockNumber) 指定块高

* **返回值**

```android
Request<?, PlatonGetStorageAt>
```

PlatonGetStorageAt属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String address = "";
Request <?, PlatonGetStorageAt> request = currentValidWeb3j.platonGetStorageAt(address ,BigInteger.ZERO,DefaultBlockParameterName.LATEST );
String req = request.send().getData();
```

<a name="platonGetBlockTransactionCountByHash"></a>
### 13.platonGetBlockTransactionCountByHash

> 根据区块hash查询区块中交易个数

* **参数**
	- String : blockHash 区块hash

* **返回值**

```android
Request<?, PlatonGetBlockTransactionCountByHash>
```

PlatonGetBlockTransactionCountByHash属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String blockhash = "";
Request <?, PlatonGetBlockTransactionCountByHash> request = currentValidWeb3j.platonGetBlockTransactionCountByHash(blockhash);
BigInteger req = request.send().getTransactionCount();
```

<a name="platonGetTransactionCount"></a>
### 14.platonGetTransactionCount

> 根据地址查询该地址发送的交易个数

* **参数**
    - String : address 查询地址
    - DefaultBlockParameter:
      -  DefaultBlockParameterName.LATEST  最新块高(默认)
      -  DefaultBlockParameterName.EARLIEST 最低块高
      -  DefaultBlockParameterName.PENDING 未打包交易
      -  DefaultBlockParameter.valueOf(BigInteger blockNumber) 指定块高

* **返回值**

```android
Request<?, PlatonGetTransactionCount>
```

PlatonGetTransactionCount属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String address = "";
Request <?, PlatonGetTransactionCount> request = currentValidWeb3j.platonGetTransactionCount(address,DefaultBlockParameterName.LATEST);
BigInteger req = request.send().getTransactionCount();
```

<a name="platonGetBlockTransactionCountByNumber"></a>
### 15.platonGetBlockTransactionCountByNumber

> 根据区块块高，返回块高中的交易总数

* **参数**
    - DefaultBlockParameter:
      -  DefaultBlockParameterName.LATEST  最新块高(默认)
      -  DefaultBlockParameterName.EARLIEST 最低块高
      -  DefaultBlockParameterName.PENDING 未打包交易
      -  DefaultBlockParameter.valueOf(BigInteger blockNumber) 指定块高

* **返回值**

```android
Request<?, PlatonGetBlockTransactionCountByNumber>
```

PlatonGetBlockTransactionCountByNumber属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonGetBlockTransactionCountByNumber> request = currentValidWeb3j.platonGetBlockTransactionCountByNumber(DefaultBlockParameterName.LATEST);
BigInteger req = request.send().getTransactionCount();
```

<a name="platonGetCode"></a>
### 16.platonGetCode

>  返回给定地址的代码

* **参数**
    - String ： address 地址/合约

    - DefaultBlockParameter:
      -  DefaultBlockParameterName.LATEST  最新块高(默认)
      -  DefaultBlockParameterName.EARLIEST 最低块高
      -  DefaultBlockParameterName.PENDING 未打包交易
      -  DefaultBlockParameter.valueOf(BigInteger blockNumber) 指定块高

* **返回值**

```android
Request<?, PlatonGetCode>
```

PlatonGetCode属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String address = "";
Request <?, PlatonGetCode> request = currentValidWeb3j.platonGetCode(address,DefaultBlockParameterName.LATEST);
String req = request.send().getCode();
```

<a name="platonSign"></a>
### 17.platonSign

>  数据签名

* **参数**
    - String ： address   地址
    - String :  sha3HashOfDataToSign  待签名数据

* **返回值**

```android
Request<?, PlatonSign>
```

PlatonSign属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String address = "";
String sha3HashOfDataToSign   = "";
Request <?, PlatonSign> request = currentValidWeb3j.platonSign(address,DefaultBlockParameterName.LATEST);
String req = request.send().getSignature();
```

备注：地址必须提前先解锁

<a name="platonSendTransaction"></a>
### 18.platonSendTransaction

>  发送服务代签名交易

* **参数**

    - Transaction : Transaction: 交易结构
      - String : from : 交易发送地址
      - String : to : 交易接收方地址
      - BigInteger ： gas ：  本次交易gas用量上限
      - BigInteger ： gasPrice ： gas价格
      - BigInteger ：value ： 转账金额
      - String ：data ： 上链数据
      - BigInteger ：nonce ： 交易唯一性标识
        - 调用platonGetTransactionCount，获取from地址作为参数，获取到该地址的已发送交易总数
        - 每次使用该地址nonce +1

* **返回值**

```android
Request<?, PlatonSendTransaction>
```

PlatonSendTransaction属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Transaction transaction = new Transaction("from","to",BigInteger.ZERO,BigInteger.ZERO,BigInteger.ZERO,"data ",BigInteger.ONE);
Request <?, PlatonSendTransaction> request = currentValidWeb3j.platonSendTransaction(transaction);
String req = request.send().getTransactionHash();
```

<a name="platonSendRawTransaction"></a>
### 19.platonSendRawTransaction

>  发送交易

* **参数**
	- String : data : 钱包签名后数据

* **返回值**

```android
Request<?, PlatonSendTransaction>
```

PlatonSendTransaction属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String  data = "";
Request <?, PlatonSendTransaction> request = currentValidWeb3j.platonSendRawTransaction(data);
String req = request.send().getTransactionHash();
```

<a name="platonCall"></a>
### 20.platonCall

>   执行一个消息调用交易，消息调用交易直接在节点旳VM中执行而 不需要通过区块链的挖矿来执行

* **参数**
    - Transaction : Transaction: 交易结构
      - String : from : 交易发送地址
      - String : to : 交易接收方地址
      - BigInteger ： gas ：  本次交易gas用量上限
      - BigInteger ： gasPrice ： gas价格
      - BigInteger ：value ： 转账金额
      - String ：data ： 上链数据
      - BigInteger ：nonce ： 交易唯一性标识
        - 调用platonGetTransactionCount，获取from地址作为参数，获取到该地址的已发送交易总数
        - 每次使用该地址nonce +1

* **返回值**

```android
Request<?, PlatonCall>
```

PlatonCall属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Transaction transaction = new Transaction("from","to",BigInteger.ZERO,BigInteger.ZERO,BigInteger.ZERO,"data ",BigInteger.ONE);
Request <?, PlatonSendTransaction> request = currentValidWeb3j.platonCall(transaction);
String req = request.send().getValue();
```

<a name="platonEstimateGas"></a>
### 21.platonEstimateGas

>   估算合约方法gas用量

* **参数**
    - Transaction : Transaction: 交易结构
      - String : from : 交易发送地址
      - String : to : 交易接收方地址
      - BigInteger ： gas ：  本次交易gas用量上限
      - BigInteger ： gasPrice ： gas价格
      - BigInteger ：value ： 转账金额
      - String ：data ： 上链数据
      - BigInteger ：nonce ： 交易唯一性标识
        - 调用platonGetTransactionCount，获取from地址作为参数，获取到该地址的已发送交易总数
        - 每次使用该地址nonce +1

* **返回值**

```android
Request<?, PlatonEstimateGas>
```

PlatonEstimateGas属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Transaction transaction = new Transaction("from","to",BigInteger.ZERO,BigInteger.ZERO,BigInteger.ZERO,"data ",BigInteger.ONE);
Request <?, PlatonEstimateGas> request = currentValidWeb3j.platonEstimateGas(transaction);
BigInteger req = request.send().getAmountUsed();
```

<a name="platonGetBlockByHash"></a>
### 22.platonGetBlockByHash

>  根据区块hash查询区块信息

* **参数**
    - String ： blockHash  区块hash
    - boolean :
      -  true ： 区块中带有完整的交易列表
      -  false： 区块中只带交易hash列表

* **返回值**

```android
Request<?, PlatonBlock>
```

PlatonBlock属性中的Block即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String blockHash  = "";

Request <?, PlatonBlock> request = currentValidWeb3j.platonGetBlockByHash(blockHash,true);
Block req = request.send().getBlock();
```

<a name="platonGetBlockByNumber"></a>
### 23.platonGetBlockByNumber

>  根据区块高度查询区块信息

* **参数**
    - DefaultBlockParameter:
      -  DefaultBlockParameterName.LATEST  最新块高(默认)
      -  DefaultBlockParameterName.EARLIEST 最低块高
      -  DefaultBlockParameterName.PENDING 未打包交易
      -  DefaultBlockParameter.valueOf(BigInteger blockNumber) 指定块高
    - boolean :
      -  true ： 区块中带有完整的交易列表
      -  false： 区块中只带交易hash列表

* **返回值**

```android
Request<?, PlatonBlock>
```

PlatonBlock属性中的Block即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonBlock> request = currentValidWeb3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO) ,true);
Block req = request.send().getBlock();
```

<a name="platonGetTransactionByBlockHashAndIndex"></a>
### 24.platonGetTransactionByBlockHashAndIndex

>  根据区块hash查询区块中指定序号的交易

* **参数**
    - String : blockHash  区块hash
    - BigInteger ： transactionIndex  交易在区块中的序号

* **返回值**

```android
Request<?, PlatonTransaction>
```

PlatonTransaction属性中的Transaction即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String blockHash    = "";
Request <?, PlatonTransaction> request = currentValidWeb3j.platonGetTransactionByHash(blockHash,BigInteger.ZERO);
Optional<Transaction> req = request.send().getTransaction();
```

<a name="platonGetTransactionByBlockNumberAndIndex"></a>
### 25.platonGetTransactionByBlockNumberAndIndex

>  根据区块高度查询区块中指定序号的交易

* **参数**
    - DefaultBlockParameter:
      -  DefaultBlockParameterName.LATEST  最新块高(默认)
      -  DefaultBlockParameterName.EARLIEST 最低块高
      -  DefaultBlockParameterName.PENDING 未打包交易
      -  DefaultBlockParameter.valueOf(BigInteger blockNumber) 指定块高
    - BigInteger ： transactionIndex  交易在区块中的序号

* **返回值**

```android
Request<?, PlatonTransaction>
```

PlatonTransaction属性中的Transaction即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String blockHash    = "";
Request <?, PlatonTransaction> request = currentValidWeb3j.platonGetTransactionByHash(DefaultBlockParameter.valueOf(BigInteger.ZERO) ,BigInteger.ZERO);
Optional<Transaction> req = request.send().getTransaction();
```

<a name="platonGetTransactionReceipt"></a>
### 26.platonGetTransactionReceipt

>  根据交易hash查询交易回执

* **参数**
	- String : transactionHash  交易hash

* **返回值**

```android
Request<?, PlatonGetTransactionReceipt>
```

PlatonGetTransactionReceipt属性中的Transaction即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String blockHash    = "";
Request <?, PlatonGetTransactionReceipt> request = currentValidWeb3j.platonGetTransactionReceipt(DefaultBlockParameter.valueOf(BigInteger.ZERO) ,BigInteger.ZERO);
Optional<TransactionReceipt> req = request.send().getTransactionReceipt();
```

<a name="platonNewFilter"></a>
### 27.platonNewFilter

>   创建一个过滤器，以便在客户端接收到匹配的whisper消息时进行通知

* **参数**
    - PlatonFilter:  PlatonFilter :
      - SingleTopic :

* **返回值**

```android
Request<?, PlatonFilter>
```

PlatonFilter属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
PlatonFilter filter = new PlatonFilter();
filter.addSingleTopic("");
Request <?, PlatonFilter> request = currentValidWeb3j.platonNewFilter(filter);
BigInteger req = request.send().getFilterId();
```

<a name="platonNewBlockFilter"></a>
### 28.platonNewBlockFilter

>   在节点中创建一个过滤器，以便当新块生成时进行通知。要检查状态是否变化

* **参数**

  无

* **返回值**

```android
Request<?, PlatonFilter>
```

PlatonFilter属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3j.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonFilter> request = currentValidWeb3j.platonNewBlockFilter();
BigInteger req = request.send().getFilterId();
```

<a name="platonNewPendingTransactionFilter"></a>
### 29.platonNewPendingTransactionFilter

>    在节点中创建一个过滤器，以便当产生挂起交易时进行通知。 要检查状态是否发生变化

* **参数**

  无

* **返回值**

```android
Request<?, PlatonFilter>
```

PlatonFilter属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonFilter> request = currentValidWeb3j.platonNewPendingTransactionFilter();
BigInteger req = request.send().getFilterId();
```

<a name="platonNewPendingTransactionFilter"></a>
### 30.platonNewPendingTransactionFilter

>  写在具有指定编号的过滤器。当不在需要监听时，总是需要执行该调用

* **参数**

  无

* **返回值**

```android
Request<?, PlatonFilter>
```

PlatonFilter属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonFilter> request = currentValidWeb3j.platonNewPendingTransactionFilter();
BigInteger req = request.send().getFilterId();
```

<a name="platonUninstallFilter"></a>
### 31.platonUninstallFilter

>     写在具有指定编号的过滤器。当不在需要监听时，总是需要执行该调用

* **参数**
	- BigInteger  : filterId :  过滤器编号

* **返回值**

```android
Request<?, PlatonUninstallFilter>
```

PlatonUninstallFilter属性中的boolean即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonUninstallFilter> request = currentValidWeb3j.platonNewPendingTransactionFilter(BigInteger.ZERO);
boolean req = request.send().isUninstalled();
```

<a name="platonGetFilterChanges"></a>
### 32.platonGetFilterChanges

>    轮询指定的过滤器，并返回自上次轮询之后新生成的日志数组

* **参数**
	- BigInteger  : filterId :  过滤器编号

* **返回值**

```android
Request<?, PlatonLog>
```

PlatonLog属性中的LogResult数组即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonLog> request = currentValidWeb3j.platonGetFilterChanges(BigInteger.ZERO);
List<PlatonLog.LogResult> req = request.send().getLogs();
```

<a name="platonGetFilterLogs"></a>
### 33.platonGetFilterLogs

>     轮询指定的过滤器，并返回自上次轮询之后新生成的日志数组。

* **参数**
	- BigInteger  : filterId :  过滤器编号

* **返回值**

```android
Request<?, PlatonLog>
```

PlatonLog属性中的LogResult数组即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request <?, PlatonLog> request = currentValidWeb3j.platonGetFilterLogs(BigInteger.ZERO);
List<PlatonLog.LogResult> req = request.send().getLogs();
```

<a name="platonGetLogs"></a>
### 34.platonGetLogs

>    返回指定过滤器中的所有日志

* **参数**
    - PlatonFilter:  PlatonFilter :
      - SingleTopic :

* **返回值**

```android
Request<?, PlatonLog>
```

PlatonLog属性中的BigInteger即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
PlatonFilter filter = new PlatonFilter();
filter.addSingleTopic("");
Request <?, PlatonLog> request = currentValidWeb3j.platonGetLogs(filter);
List<LogResult> = request.send().getLogs();
```

<a name="platonPendingTx"></a>
### 35.platonPendingTx
>查询待处理交易

* **参数**

   无

* **返回值**

```android
Request<?, PlatonPendingTransactions>
```

PlatonPendingTransactions属性中的transactions即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request<?, PlatonPendingTransactions> req = currentValidWeb3j.platonPendingTx();
EthPendingTransactions res = req.send();
List<Transaction> transactions = res.getTransactions();
```

<a name="dbPutString"></a>
### 36.dbPutString

>   在本地数据库中存入字符串。

* **参数**
    - String :  databaseName :   数据库名称
    - String : keyName :  键名
    - String : stringToStore :   要存入的字符串

* **返回值**

```android
Request<?, DbPutString>
```

DbPutString属性中的boolean即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String databaseName;
String keyName;
String stringToStore;
Request <?, DbPutString> request = currentValidWeb3j.dbPutString(databaseName,keyName,stringToStore);
List<DbPutString> = request.send().valueStored();
```

<a name="dbGetString"></a>
### 37.dbGetString

>    从本地数据库读取字符串

* **参数**
    - String :  databaseName :   数据库名称
    - String : keyName :  键名

* **返回值**

```android
Request<?, DbGetString>
```

DbGetString属性中的String即为对应存储数据

**示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String databaseName;
String keyName;
Request <?, DbGetString> request = currentValidWeb3j.dbGetString(databaseName,keyName);
String req  = request.send().getStoredValue();
```

<a name="dbPutHex"></a>
### 38.dbPutHex

>     将二进制数据写入本地数据库

* **参数**
    - String :  databaseName :   数据库名称
    - String : keyName :  键名
    - String : dataToStore :   要存入的二进制数据

* **返回值**

```android
Request<?, DbPutHex>
```

DbPutHex属性中的boolean即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String databaseName;
String keyName;
String dataToStore;
Request <?, DbPutHex> request = currentValidWeb3j.dbPutHex(databaseName,keyName,dataToStore);
boolean req  = request.send().valueStored();
```

<a name="dbGetHex"></a>
### 39.dbGetHex

>     从本地数据库中读取二进制数据

* **参数**
    - String :  databaseName :   数据库名称
    - String : keyName :  键名

* **返回值**

```android
Request<?, DbGetHex>
```

DbGetHex属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
String databaseName;
String keyName;
Request <?, DbGetHex> request = currentValidWeb3j.dbGetHex(databaseName,keyName);
String req  = request.send().getStoredValue();
```

<a name="platonEvidences"></a>
### 40.platonEvidences

>    返回双签举报数据

* **参数**

  无

* **出参**

| 参数    | 类型   | 描述       |
| ------- | ------ | ---------- |
| jsonrpc | string | rpc版本号  |
| id      | int    | id序号     |
| result  | string | 证据字符串 |

result为证据字符串，包含3种证据类型，分别是：duplicatePrepare、duplicateVote、duplicateViewchange
每种类型包含多个证据，所以是数组结构，解析时需注意。

* **duplicatePrepare**

```json
{
    "prepare_a":{
        "epoch":0, 			//共识轮epoch值
        "view_number":0,	//共识轮view值
        "block_hash":"0xf41006b64e9109098723a37f9246a76c236cd97c67a334cfb4d54bc36a3f1306",
        //区块hash
        "block_number":500,		//区块number
        "block_index":0,		//区块在一轮view中的索引值
        "validate_node":{
            "index":0,			//验证人在一轮epoch中的索引值
            "address":"0x0550184a50db8162c0cfe9296f06b2b1db019331",		//验证人地址
            "NodeID":"77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050",		//验证人nodeID
            "blsPubKey":"5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811"		//验证人bls公钥
        },
        "signature":"0xa7205d571b16696b3a9b68e4b9ccef001c751d860d0427760f650853fe563f5191f2292dd67ccd6c89ed050182f19b9200000000000000000000000000000000"	//消息签名
    }
 }
```

* **duplicateVote**

```json
{
    "voteA":{
        "epoch":0, 			//共识轮epoch值
        "view_number":0,	//共识轮view值
        "block_hash":"0xf41006b64e9109098723a37f9246a76c236cd97c67a334cfb4d54bc36a3f1306",
        //区块hash
        "block_number":500,		//区块number
        "block_index":0,		//区块在一轮view中的索引值
        "validate_node":{
            "index":0,			//验证人在一轮epoch中的索引值
            "address":"0x0550184a50db8162c0cfe9296f06b2b1db019331",		//验证人地址
            "NodeID":"77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050",		//验证人nodeID
            "blsPubKey":"5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811"		//验证人bls公钥
        },
        "signature":"0xa7205d571b16696b3a9b68e4b9ccef001c751d860d0427760f650853fe563f5191f2292dd67ccd6c89ed050182f19b9200000000000000000000000000000000"	//消息签名
    }
 }
```

* **duplicateViewchange**

```json
{
    "viewA":{
        "epoch":0, 			//共识轮epoch值
        "view_number":0,	//共识轮view值
        "block_hash":"0xf41006b64e9109098723a37f9246a76c236cd97c67a334cfb4d54bc36a3f1306",
        //区块hash
        "block_number":500,		//区块number
        "block_index":0,		//区块在一轮view中的索引值
        "validate_node":{
            "index":0,			//验证人在一轮epoch中的索引值
            "address":"0x0550184a50db8162c0cfe9296f06b2b1db019331",		//验证人地址
            "NodeID":"77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050",		//验证人nodeID
            "blsPubKey":"5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811"		//验证人bls公钥
        },
        "signature":"0xa7205d571b16696b3a9b68e4b9ccef001c751d860d0427760f650853fe563f5191f2292dd67ccd6c89ed050182f19b9200000000000000000000000000000000"	//消息签名
    }
 }
```

* **返回值**

```android
Request<?, PlatonEvidences>
```

PlatonEvidences属性中的Evidences对象即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request<?, PlatonEvidences> req = currentValidWeb3j.platonEvidences();
Evidences evidences = req.send().getEvidences();
```

<a name="getProgramVersion"></a>
### 41.getProgramVersion

>    获取代码版本

* **参数**

  无

* **返回值**

```android
Request<?, AdminProgramVersion>
```

AdminProgramVersion属性中的ProgramVersion对象即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request<?, AdminProgramVersion> req = currentValidWeb3j.getProgramVersion();
ProgramVersion programVersion = req.send().getAdminProgramVersion();
```

* **ProgramVersion 对象解析**
    - BigInteger ： version ： 代码版本
    - String ： sign ： 代码版本签名

<a name="getSchnorrNIZKProve"></a>
### 42.getSchnorrNIZKProve

>    获取bls的证明

* **参数**

  无

* **返回值**

```android
Request<?, AdminSchnorrNIZKProve>
```

AdminSchnorrNIZKProve属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request<?, AdminProgramVersion> req = currentValidWeb3j.getSchnorrNIZKProve();
String res = req.send().getAdminSchnorrNIZKProve();
```

<a name="getEconomicConfig"></a>
### 43.getEconomicConfig

>    获取PlatON参数配置

* **参数**

  无

* **返回值**

```android
Request<?, DebugEconomicConfig>
```

DebugEconomicConfig属性中的String即为对应存储数据

* **示例**

```android
Web3j platonWeb3j = Web3jFactory.build(new HttpService("http://127.0.0.1:6789"));
Request<?, DebugEconomicConfig> req = currentValidWeb3j.getEconomicConfig();
String debugEconomicConfig = req.send().getEconomicConfigStr();
```

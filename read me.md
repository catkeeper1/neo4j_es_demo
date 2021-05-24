欢迎加入绿蕃茄
一、安装ek，elasticsearch、kibana版本要一致。目前都使用7.5.2
# 安装 ek 7.5.2 版本
docker pull elasticsearch:7.5.2
docker pull kibana:7.5.2
# 查看是否下载成功
docker images

二、开始搭建
# 第一步：创建2个文件夹，用于存放Elasticsearch数据。xxxxxxx为本地的目录。
mkdir -p /xxxxxxx/elk/elasticsearch/data
mkdir -p /xxxxxxx/elk/elasticsearch/plugins
# 另外可以再创建一个文件夹，用于存放docker-compose.yml文件。xxxxxxx为本地的目录。
mkdir -p /xxxxxxx/elk/docker
# 第二步：在/xxxxxxx/elk/docker创建docker-compose.yml文件。xxxxxxx为本地的目录
# 详见docker目录下的docker-compose.yml 注意：此docker-compose.yml中xxxxxxx为你之前创建的目录，请替换
# 第三步：在docker-compose.yml文件目录下启动elk
docker-compose up -d
# 第四步：访问 localhost:9200 和 localhost:5601 能够正常，说明整个ek环境已搭建完成。至此，整个EK环境已经能够搭建完成

三、docker 安装head插件
# 安装elasticsearch head插件监控管理
# 安装命令：
docker pull mobz/elasticsearch-head:5
# 启动命令：
docker run -d -p 9100:9100 docker.io/mobz/elasticsearch-head:5
# 网页打开elasticsearch-head页面，填入插件地址http://localhost:9100/，连接es地址http://localhost:9200/，一般会出现不上的状态，这是跨域拒绝访问问题。
# 解决方案：进入elasticsearch容器内部，修改elasticsearch.yml，增加跨域的配置(需要重启es才能生效)
# 输入命令：
docker ps      获得容器id
# 例如获得容器id为 2f4ed2dfe20d
# 进入容器：
docker exec -it 容器id bash
# 例子docker exec -it 2f4ed2dfe20d bash
# 修改配置：
vi config/elasticsearch.yml
# 添加内容：
http.cors.enabled: true
http.cors.allow-origin: "*"
# 详见docker目录下的config/elasticsearch.yml
# 重启es，head插件正常生效。

# 在docker上运行 docker run -d --publish=7474:7474 --publish=7687:7687 -e 'NEO4J_AUTH=neo4j/secret' --name neo4j neo4j:4.1.5
打开http://localhost:7474/browser/ 输入账号neo4j 密码secret 连接

# 安装es可视化图形数据库 可crud
docker run --name dejavu -p 1358:1358 -d appbaseio/dejavu:3.4.2
打开http://localhost:1358/  输入账号http://localhost:9200 密码 索引库名字 进行连接
# 在测试类测试es 首先运行一下docker命令
docker run -d --name elasticsearch02 -p 9201:9200 -p 9301:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx512m" elasticsearch:7.5.2
# 在测试类测试neo4j 首先运行一下docker命令
docker run -d -p 7475:7474 -p 7688:7687 -e 'NEO4J_AUTH=neo4j/secret' --name neo4j07 neo4j:4.1.5
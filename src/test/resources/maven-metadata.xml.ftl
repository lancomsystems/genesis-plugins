<metadata>
    <#assign actualVersions = (versions ! [])>
    <groupId>org.example</groupId>
    <artifactId>test-project</artifactId>
    <versioning>
        <latest>${actualVersions?last}</latest>
        <release>${actualVersions?last}</release>
        <versions>
            <#list actualVersions as version>
            <version>${version}</version>
            </#list>
        </versions>
        <lastUpdated>20210125033325</lastUpdated>
    </versioning>
</metadata>